package com.shulian.netty.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * 线程安全的 handler 可以做多个pipeline里面共享，通过打上注解 Sharable。但是还需要保证这样的
 * handler 是无状态的。
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2020-09-09 12:00
 * @since jdk1.8
 */
@ChannelHandler.Sharable
@Slf4j
public class SharableHandler extends ChannelInboundHandlerAdapter {


    /**
     * The problem with this code is that it has state; namely the instance variable count,
     * which tracks the number of method invocations. Adding an instance of this class to
     * the ChannelPipeline will very likely produce errors when it’s accessed by concurrent
     * channels. (Of course, this simple case could be corrected by making channelRead()
     * synchronized.)
     * In summary, use @Sharable only if you’re certain that your ChannelHandler is
     * thread-safe.
     */
    private int count;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        count++;
        ctx.fireChannelRead(msg);
//        ctx.name()

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //处理 inbound 事件的异常，典型的方式是重写  exceptionCaught 方法，如果所有的inboundHandler
//        都不处理异常，到了pipeline尾部，会打印异常日志并标记这个异常未做处理
        log.warn("DiscardOutboundHandler捕获异常", cause);
        ctx.close();
    }

}
