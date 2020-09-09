package com.shulian.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

/**
 * 演示 ChannelHandlerContext 使用
 *
 *  无论是用  channel,还是channelpipeline来调用write方法，效果都是一样的，让事件流一直沿着穿过pipeline。但是
 *  在 channelHandler 和 下一个 channelHandler  之间的事件流动是靠 ChannelHandlerContext 来触发的
 * @author zhangjuwa
 * @apiNote
 * @date 2020-09-09 10:50
 * @since jdk1.8
 */
@ChannelHandler.Sharable
public class CtxTestHandler extends ChannelInboundHandlerAdapter {

    /**
     * In the following listing you acquire a reference to the Channel from a ChannelHandlerContext.
     * Calling write() on the Channel causes a write event to flow all the way through the pipeline.
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = ctx.channel();
        ByteBufAllocator allocator = ctx.alloc();
        ByteBuf buffer = allocator.buffer(512);
        buffer.writeBytes("netty in action!".getBytes(CharsetUtil.UTF_8));
        channel.write(buffer);

//        The next listing shows a similar example, but writing this time to a ChannelPipeline.
//        Again, the reference is retrieved from the ChannelHandlerContext.
        ChannelPipeline pipeline = ctx.pipeline();
        pipeline.write(buffer);

        ReferenceCountUtil.release(buffer);
    }


}
