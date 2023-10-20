package com.shulian.netty.chat;

import com.shulian.netty.handler.HttpRequestHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * The following listing shows our ChannelInboundHandler for TextWebSocketFrames,
 * which will also track all the active WebSocket connections in its ChannelGroup.
 *
 * The TextWebSocketFrameHandler has a very small set of responsibilities. When the
 * WebSocket handshake with the new client has completed successfully B, it notifies all
 * connected clients by writing to all the Channels in the ChannelGroup, then it adds the
 * new Channel to the ChannelGroup .
 *  If a TextWebSocketFrame is received , it calls retain() on it and uses writeAndFlush()
 *  to transmit it to the ChannelGroup so that all connected WebSocket
 * Channels will receive it.
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2020-09-25 00:24
 * @since jdk1.8
 */
@Slf4j
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final ChannelGroup group;

    public TextWebSocketFrameHandler(ChannelGroup group) {
        this.group = group;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        log.info("当前通道{}接收的内容={}", ctx.channel(), msg.text());
        group.writeAndFlush(msg.retain());
    }

    /**
     * Overrides userEventTriggered() to handle custom events
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        If the event indicates that
//        the handshake was successful removes the HttpRequestHandler from the
//        ChannelPipeline because no  further HTTP messages will be received
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            ctx.pipeline().remove(HttpRequestHandler.class);
            //通道组的所有通道的写操作，简化了一个通道组里面所有通道的写操作,然后把当前通道也加入通道组
            group.writeAndFlush("clent " + ctx.channel() + "joined!");
            group.add(ctx.channel());
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

//    @Override
//    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        group.
//    }
}
