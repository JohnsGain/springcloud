package com.shulian.netty.chapter8;

import io.netty.channel.*;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.ssl.SslHandler;

/**
 * Netty offers many ways to use WebSockets, but the easiest one, which works for most
 * users, is using the WebSocketServerProtocolHandler when writing a WebSockets server, as
 * shown in listing 8.6. This handles the handshake and also the CloseWebSocketFrame,
 * PingWebSocketFrame, and PongWebSocketFrame for you.
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2020-04-14 11:22
 * @since jdk1.8
 */
public class WebSocketServerInitializer extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
//        If you want to support Secure-WebSocket, it’s as easy as adding the SslHandler as the first
//        ChannelHandler in the ChannelPipeline.
//        pipeline.addFirst("ssl", new SslHandler(sslengine));

        pipeline.addLast(new HttpServerCodec(),
                new HttpObjectAggregator(512 * 1024),
                new WebSocketServerProtocolHandler("/websocket"),
                new TextFrameHandler(),
                new BinaryFrameHandler(),
                new ContinuationFrameHandler());
    }

    public static final class TextFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
// Handle text frame
        }
    }

    public static final class BinaryFrameHandler extends SimpleChannelInboundHandler<BinaryWebSocketFrame> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, BinaryWebSocketFrame msg) throws Exception {
// Handle binary frame
        }
    }

    public static final class ContinuationFrameHandler extends SimpleChannelInboundHandler<ContinuationWebSocketFrame> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ContinuationWebSocketFrame msg) throws Exception {
// Handle continuation frame
        }
    }

}
