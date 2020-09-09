package com.shulian.netty.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;

/**
 * On the outbound side, if you handle a write() operation and discard a message,
 * you’re responsible for releasing it. The next listing shows an implementation that discards all written data.
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2020-09-08 18:30
 * @since jdk1.8
 */
@ChannelHandler.Sharable
public class DiscardOutboundHandler extends ChannelOutboundHandlerAdapter {
    /**
     * It’s important not only to release resources but also to notify the ChannelPromise.
     * Otherwise a situation might arise where a ChannelFutureListener has not been notified about a message that has been handled.
     *  In sum, it is the responsibility of the user to call ReferenceCountUtil.release() if
     * a message is consumed or discarded and not passed to the next ChannelOutboundHandler in the ChannelPipeline.
     * If the message reaches the actual transport layer, it
     * will be released automatically when it’s written or the Channel is closed
     * @param ctx
     * @param msg
     * @param promise
     * @throws Exception
     */
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ctx.write(msg);
        ReferenceCountUtil.release(msg);
//        Notifies ChannelPromise that data was handled
        promise.setSuccess();
    }

}
