package com.shulian.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2020-03-24 10:55
 * @since jdk1.8
 */
@ChannelHandler.Sharable
@Slf4j
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * @param ctx 这个方法返回之后  bytebuf里面的消息不会被释放
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        log.info("server received {}", byteBuf.toString(CharsetUtil.UTF_8));
        ctx.write(byteBuf);
//        ctx.pipeline().fireChannelActive()
//        ctx.fireChannelActive()
//        ReferenceCountUtil.release(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.info("xxx");
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                //在写操作完成之后执行关闭channel
                .addListener(ChannelFutureListener.CLOSE);
        log.info("度完成");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("xxxxx");
        log.warn("异常", cause);
        //关闭当前通道
        ctx.close();
    }

}
