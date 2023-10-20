package com.shulian.netty.channelinitializer;

import io.netty.channel.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedStream;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.io.File;
import java.io.FileInputStream;

/**
 *  the implementation most often used
 * in practice. The class shown is instantiated with a File and an SslContext. When initChannel() is called,
 * it initializes the channel with the chain of handlers shown.
 *  When the channel becomes active, the WriteStreamHandler will write data from the
 * file chunk by chunk as a ChunkedStream. The data will be encrypted by the SslHandler
 * before being transmitted.
 * @author zhangjuwa
 * @apiNote
 * @date 2020-09-22 19:34
 * @since jdk1.8
 */
public class ChunkedWriteHandlerInitializer extends ChannelInitializer<Channel> {

    private final File file;
    private final SslContext sslCtx;

    public ChunkedWriteHandlerInitializer(File file, SslContext sslCtx) {
        this.file = file;
        this.sslCtx = sslCtx;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new SslHandler(sslCtx.newEngine(ch.alloc())));
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new WriteStreamHandler());
    }

    private class WriteStreamHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ctx.fireChannelActive();
            ctx.writeAndFlush(new ChunkedStream(new FileInputStream(file)));
        }
    }
}
