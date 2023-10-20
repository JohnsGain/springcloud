package com.shulian.netty.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedFile;
import io.netty.handler.stream.ChunkedInput;
import io.netty.handler.stream.ChunkedWriteHandler;

import javax.net.ssl.SSLEngine;

/**
 * 带SSL的聊天室服务的通道初始化器
 *In a real-life scenario, you’d soon be asked to add encryption to this server. With Netty
 * this is just a matter of adding an SslHandler to the ChannelPipeline and configuring
 * it. The following listing shows how this can be done by extending our ChatServerInitializer
 * to create a SecureChatServerInitializer.
 * @author zhangjuwa
 * @apiNote
 * @date 2020-09-25 16:34
 * @since jdk1.8
 */
public class SecureChatServerInitializer extends ChatServerInitializer {

    private final SslContext context;


    public SecureChatServerInitializer(SslContext context, ChannelGroup group) {
        super(group);
        this.context = context;
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
     * @param ch
     * @throws Exception
     * @apiNote 当websocket连接建立完成之后，WebSocketServerProtocolHandler会自动把pipeline里面不需要的handlers移除以
     * 最大化性能。移除的handlers 包括HttpRequestHandler，the WebSocketServerProtocolHandler replaces
     * the HttpRequestDecoder with a WebSocketFrameDecoder and the HttpResponseEncoder with a WebSocketFrameEncoder.
     */
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        super.initChannel(ch);
        SSLEngine sslEngine = context.newEngine(ch.alloc());
        pipeline.addFirst(new SslHandler(sslEngine));
    }

}
