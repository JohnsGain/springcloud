package com.shulian.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2020-09-27 23:22
 * @since jdk1.8
 */
@Slf4j
public class HeartbeatHandler extends ChannelInboundHandlerAdapter {


    private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(
            Unpooled.copiedBuffer("HEARTBEAT", CharsetUtil.UTF_8));


    //        This example illustrates how to employ IdleStateHandler to test whether the remote
//        peer is still alive and to free up resources by closing the connection if it is not.
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            log.info("接收到闲置状态事件={}", evt);
            ctx.close();
            ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate())
                    //当这个操作执行结果是失败或者取消，就关闭通道
                    .addListener(ChannelFutureListener.CLOSE_ON_FAILURE);

        } else {
            super.userEventTriggered(ctx, evt);
        }
    }



}
