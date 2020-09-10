package com.shulian.netty.handler;

import io.netty.channel.*;
import io.netty.util.concurrent.ScheduledFuture;

import java.util.concurrent.TimeUnit;

/**
 * The ScheduledExecutorService implementation has limitations, such as the fact that
 * extra threads are created as part of pool management. This can become a bottleneck
 * if many tasks are aggressively scheduled. Netty addresses this by implementing scheduling
 * using the channel’s EventLoop, as shown in the following listing
 * @author zhangjuwa
 * @apiNote
 * @since jdk1.8
 */
@ChannelHandler.Sharable
public class SchedulingTaskHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = ctx.channel();
        EventLoop eventExecutors = channel.eventLoop();
        ScheduledFuture<?> scheduledFuture = eventExecutors.scheduleAtFixedRate(() -> System.out.println(System.currentTimeMillis()),
                60, 60, TimeUnit.SECONDS);

//        To cancel or check the state of an execution, use the ScheduledFuture that’s returned
//        for every asynchronous operation. This listing shows a simple cancellation operation.

        //取消任务调度 ，参数为false表示 正在执行的任务不会中断，但是下一个不会再执行
        scheduledFuture.cancel(false);
    }
}
