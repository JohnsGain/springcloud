package com.shulian.netty.channelinitializer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * 限制连接和超时处理
 * The most used in practice is the IdleStateHandler, so let’s focus on it.
 * Listing 8.9 shows how you can use the IdleStateHandler to get notified if you haven’t
 * received or sent data for 60 seconds. If this is the case, a heartbeat will be written to the
 * remote peer, and if this fails the connection is closed
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2020-04-14 12:22
 * @since jdk1.8
 */
public class IdleStateHandlerInitializer extends ChannelInitializer<Channel> {

    /**
     * @param ch The most used in practice is the IdleStateHandler, so let’s focus on it.
     *           * Listing 8.9 shows how you can use the IdleStateHandler to get notified if you haven’t
     *           * received or sent data for 60 seconds. If this is the case, a heartbeat will be written to the
     *           * remote peer, and  if there is no response the connection is closed.
     * @throws Exception
     */
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new IdleStateHandler(0, 0, 60, TimeUnit.SECONDS),
                new HeartbeatHandler());
    }

    public static final class HeartbeatHandler extends ChannelInboundHandlerAdapter {

        private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(
                Unpooled.copiedBuffer("HEARTBEAT", CharsetUtil.UTF_8));

        //        This example illustrates how to employ IdleStateHandler to test whether the remote
//        peer is still alive and to free up resources by closing the connection if it is not.
        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof IdleStateEvent) {
                ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate())
                        //当这个操作执行结果是失败或者取消，就关闭通道
                        .addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            } else {
                super.userEventTriggered(ctx, evt);
            }
        }
    }

}
