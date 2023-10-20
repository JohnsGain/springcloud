package com.shulian.netty.channelinitializer;

import io.netty.channel.*;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * presents a simple example using WebSocketServerProtocolHandler. This class handles the protocol
 *  upgrade handshake as well as the
 * three control frames—Close, Ping, and Pong. Text and Binary data frames will be
 * passed along to the next handlers (implemented by you) for processing
 *
 *
 * Secure WebSocket
 * To add security to WebSocket, simply insert the SslHandler as the first ChannelHandler in the pipeline
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
