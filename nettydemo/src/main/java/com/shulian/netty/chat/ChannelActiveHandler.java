package com.shulian.netty.chat;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * SimpleChannelInboundHandler vs. ChannelInboundHandler
 * You may be wondering why we used SimpleChannelInboundHandler in the client instead of the
 * ChannelInboundHandlerAdapter we used in the EchoServerHandler. This has to do with the
 * interaction of two factors: how our business logic processes messages and how Netty manages
 * resources.
 * In the client, when channelRead0() completes, we have the incoming message and we are done
 * with it. When this method returns, SimpleChannelInboundHandler takes care of releasing the
 * reference to the ByteBuf that holds the message.
 * In EchoServerHandler, on the other hand, we still have to echo the incoming message back to
 * the sender, and the write() operation, which is asynchronous, may not complete until after
 * channelRead() returns (see item 2 in Listing 2.2). For this reason we use
 * ChannelInboundHandlerAdapter, which does not release the message at this point. Finally, the
 * message is released in channelReadComplete() when we call ctxWriteAndFlush() (item 3).
 * Chapters 5 and 6 will cover message resource management in more detail.
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2020-03-24 16:42
 * @since jdk1.8
 */
@ChannelHandler.Sharable
@Slf4j
public class ChannelActiveHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("连接成功!!!!");
//        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!!!!!!", CharsetUtil.UTF_8));
        ctx.fireChannelActive();
        ctx.pipeline().remove(this);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
