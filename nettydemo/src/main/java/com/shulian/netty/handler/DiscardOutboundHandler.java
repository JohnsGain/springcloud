package com.shulian.netty.handler;

import io.netty.channel.*;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * On the outbound side, if you handle a write() operation and discard a message,
 * you’re responsible for releasing it. The next listing shows an implementation that discards all written data.
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2020-09-08 18:30
 * @since jdk1.8
 */
@Slf4j
@ChannelHandler.Sharable
public class DiscardOutboundHandler extends ChannelOutboundHandlerAdapter {
    /**
     * It’s important not only to release resources but also to notify the ChannelPromise.
     * Otherwise a situation might arise where a ChannelFutureListener has not been notified about a message that has been handled.
     * In sum, it is the responsibility of the user to call ReferenceCountUtil.release() if
     * a message is consumed or discarded and not passed to the next ChannelOutboundHandler in the ChannelPipeline.
     * If the message reaches the actual transport layer, it
     * will be released automatically when it’s written or the Channel is closed
     *
     * @param ctx
     * @param msg
     * @param promise
     * @throws Exception
     */
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ChannelFuture future = ctx.write(msg);
        //处理 outbound 事件异常，可以通过注册监听器到future里面，outboundHandler的几乎所有方法都带有一个参数 channelpromise,
        // channelpromise 是 ChannelFuture 的子接口
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                    Throwable cause = future.cause();
                    log.warn("DiscardOutboundHandler捕获异常", cause);
                    future.channel().close();
                }
            }
        });

        //The second option is to add a ChannelFutureListener to the ChannelPromise that is
        //passed as an argument to the ChannelOutboundHandler methods.  和 上面 把监听器注册到 future  有相同的效果
        promise.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                    Throwable cause = future.cause();
                    log.warn("DiscardOutboundHandler捕获异常", cause);
                    future.channel().close();
                }
            }
        });

        ReferenceCountUtil.release(msg);
//        Notifies ChannelPromise that data was handled
        promise.setSuccess();
    }

}
