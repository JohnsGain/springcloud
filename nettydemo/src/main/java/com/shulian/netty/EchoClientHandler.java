package com.shulian.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
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
 * @author zhangjuwa
 * @apiNote
 * @date 2020-03-24 16:42
 * @since jdk1.8
 */
@ChannelHandler.Sharable
@Slf4j
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    /**
     * @param ctx 这个方法返回之后  保存到bytebuf里面的消息就会被释放
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        log.info("Client received: "+msg.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("连接成功!!!!");
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!!!!!!", CharsetUtil.UTF_8));

        //从当前上下文的下一个handler节点开始传递消息，找到具体的处理器来处理消息，如果一直没有，就会传到TailHandler，说明没有
        //处理器处理消息，随着处理器链传递的消息占用内存也一直没释放，所以在TailHandler就会打印日志，然后释放消息
//        ctx.fireChannelRead()

        //表明是处理器链的HeadHander节点开始传递
//        ctx.pipeline().fireChannelRead();

        //写法1
//        ctx.channel().write("test data");
        //写法2:不会即刻把消息写到通道，会先存到缓冲区。 还要调用flush方法才能将缓冲区数据刷到channel中, 或者直接调用writeAndFlush方法
//        ctx.write("test data");

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
