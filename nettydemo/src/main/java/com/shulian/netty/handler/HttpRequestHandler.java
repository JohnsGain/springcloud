package com.shulian.netty.handler;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * the code for this HttpRequestHandler, which
 * extends SimpleChannelInboundHandler for FullHttpRequest messages. Notice how
 * the implementation of channelRead0() forwards any requests for the URI /ws.
 *
 * If the HTTP request references the URI /ws, HttpRequestHandler calls retain() on
 * the FullHttpRequest and forwards it to the next ChannelInboundHandler B by calling
 * fireChannelRead(msg). The call to retain() is needed because after channelRead()
 * completes, it will call release() on the FullHttpRequest to release its resources.
 * (Please refer to our discussion of SimpleChannelInboundHandler in chapter 6.)vv
 * @author zhangjuwa
 * @apiNote
 * @date 2020-09-23 19:55
 * @since jdk1.8
 */
@Slf4j
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final String wsUri;
    private static final File INDEX;

    static {
        URL location = HttpRequestHandler.class
                .getProtectionDomain().getCodeSource().getLocation();
        String path = "";
        try {
            path = location.toURI() + "index.html";
            path = !path.contains("file:") ? path : path.substring(5);
        } catch (URISyntaxException e) {
            log.warn("找不到文件", e);
        }
        INDEX = new File(path);
    }

    public HttpRequestHandler(String wsUri) {
        this.wsUri = wsUri;
    }

    /**
     * If the HTTP request references the URI /ws, HttpRequestHandler calls retain() on
     * the FullHttpRequest and forwards it to the next ChannelInboundHandler B by calling
     * fireChannelRead(msg). The call to retain() is needed because after channelRead()
     * completes, it will call release() on the FullHttpRequest to release its resources.
     * (Please refer to our discussion of SimpleChannelInboundHandler in chapter 6.)vv
     * @param ctx
     * @param httpRequest
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest httpRequest) throws Exception {
        if (wsUri.equalsIgnoreCase(httpRequest.uri())) {
//            如果是一个升级的websocket请求，就把这个请求投给下一个处理器
            ctx.fireChannelRead(httpRequest.retain());
        } else {
            if (HttpUtil.is100ContinueExpected(httpRequest)) {
                send100Continue(ctx);
            }
            RandomAccessFile randomAccessFile = new RandomAccessFile(INDEX, "r");
            HttpResponse httpResponse = new DefaultHttpResponse(httpRequest.protocolVersion(), HttpResponseStatus.OK);
            HttpHeaders headers = httpResponse.headers();
            headers.set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
//            if keepalive is request,adds the required headers
            boolean keepAlive = HttpUtil.isKeepAlive(httpRequest);
            if (keepAlive) {
                headers.set(HttpHeaderNames.CONTENT_LENGTH, randomAccessFile.length());
                headers.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            }
            ctx.write(httpResponse);
            if (ctx.pipeline().get(SslHandler.class) == null) {
                FileRegion fileRegion = new DefaultFileRegion(randomAccessFile.getChannel(), 0, randomAccessFile.length());
                ctx.write(fileRegion);
            } else {
                ctx.write(new ChunkedNioFile(randomAccessFile.getChannel()));
            }
            ChannelFuture channelFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            if (!keepAlive) {
                channelFuture.addListener(ChannelFutureListener.CLOSE);
            }
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.warn("通道异常事件, ", cause);
        ctx.close();
    }

    private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.CONTINUE);
        ctx.writeAndFlush(fullHttpResponse);
    }


}
