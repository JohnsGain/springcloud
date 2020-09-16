package com.shulian.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;

import java.net.InetSocketAddress;

/**
 * 通过 AttributeKey,可以为每个channel绑定一些属性值，这些属性值可以脱离netty环境而存在。
 * <p>
 * Consider, for example, a server application that tracks the relationship between
 * users and Channels. This can be accomplished by storing the user’s ID as an attribute
 * of a Channel. A similar technique could be used to route messages to users based on
 * their ID or to shut down a channel if there is low activity
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2020-09-15 17:14
 * @since jdk1.8
 */
public class AttributeValueClient {

    public static void main(String[] args) {
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        AttributeKey<Long> userId = AttributeKey.newInstance("id");
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress("www.baidu.com", 80))
                .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                        System.out.println("接收到数据=" + msg.toString(CharsetUtil.UTF_8));
                    }

                    @Override
                    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                        Attribute<Long> attribute = ctx.channel().attr(userId);
                        System.out.println("用户 = " + attribute.get() + "]上线");
                    }
                })
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
        bootstrap.attr(userId, 1098L);
        try {
            ChannelFuture sync = bootstrap.connect().sync();
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            Future<?> future = eventLoopGroup.shutdownGracefully();
            future.syncUninterruptibly();
        }

    }

}
