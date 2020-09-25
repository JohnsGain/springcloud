package com.shulian.netty.chat;

import com.shulian.netty.handler.HttpRequestHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedFile;
import io.netty.handler.stream.ChunkedInput;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2020-09-25 16:34
 * @since jdk1.8
 */
public class ChatServerInitializer extends ChannelInitializer<Channel> {

    private final ChannelGroup group;

    public ChatServerInitializer(ChannelGroup group) {
        this.group = group;
    }


    /**
     * so that you can send a large data stream without difficulties.
     * <p>
     * To use {@link ChunkedWriteHandler} in your application, you have to insert
     * a new {@link ChunkedWriteHandler} instance:
     * <pre>
     * {@link ChannelPipeline} p = ...;
     * p.addLast("streamer", <b>new {@link ChunkedWriteHandler}()</b>);
     * p.addLast("handler", new MyHandler());
     * </pre>
     * Once inserted, you can write a {@link ChunkedInput} so that the
     * {@link ChunkedWriteHandler} can pick it up and fetch the content of the
     * stream chunk by chunk and write the fetched chunk downstream:
     * <pre>
     * {@link Channel} ch = ...;
     * ch.write(new {@link ChunkedFile}(new File("video.mkv"));
     * </pre>
     *
     * @apiNote   当websocket连接建立完成之后，WebSocketServerProtocolHandler会自动把pipeline里面不需要的handlers移除以
     * 最大化性能。移除的handlers 包括HttpRequestHandler，the WebSocketServerProtocolHandler replaces
     * the HttpRequestDecoder with a WebSocketFrameDecoder and the HttpResponseEncoder with a WebSocketFrameEncoder.
     * @param ch
     * @throws Exception
     */
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //对http数据进行编码以支持Http协议，A combination of {@link HttpRequestDecoder} and {@link HttpResponseEncoder}
//        Decodes bytes to HttpRequest, HttpContent,
//        and LastHttpContent. Encodes HttpRequest,
//        HttpContent, and LastHttpContent to bytes.
        pipeline.addLast(new HttpServerCodec());
        //可以支持异步写出大文件数据流，而不会有内存溢出的风险。 Writes the contents of a file
        pipeline.addLast(new ChunkedWriteHandler());
        //把http请求数据的各部分组合成一个 完整的  FullHttpRequest
       /* Aggregates an HttpMessage and its following
        HttpContents into a single FullHttpRequest
        or FullHttpResponse (depending on whether
        it’s being used to handle requests or responses).
        With this installed, the next ChannelHandler
        in the pipeline will receive only full HTTP requests.*/
        pipeline.addLast(new HttpObjectAggregator(64 * 1024));
        //得到的是完整的 FullHttpRequest。Handles FullHttpRequests (those not sent to a ws URI).
        pipeline.addLast(new HttpRequestHandler("/ws"));
        //处理websocket 的控制帧 close,ping,ping。websocket连接的 握手处理，对文本，二进制数据就传到下一个cha
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
//        Handles TextWebSocketFrames and handshakecompletion events
        pipeline.addLast(new TextWebSocketFrameHandler(group));


    }
}
