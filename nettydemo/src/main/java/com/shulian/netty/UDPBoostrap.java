package com.shulian.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;

import java.net.InetSocketAddress;

/**
 * a Bootstrap can be used for connectionless protocols as well. Netty provides various
 * DatagramChannel implementations for this purpose. The only difference is that you
 * don’t call connect() but only bind(), as shown next.
 * <p>
 * Bootstrap 可以拥有启动一个 netty客户端或者基于无连接的UDP应用
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2020-09-15 18:31
 * @since jdk1.8
 */
public class UDPBoostrap {

    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        bootstrap.group(eventLoopGroup)
                .channel(NioDatagramChannel.class)
                .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                        System.out.println("接收到数据=" + msg.toString(CharsetUtil.UTF_8));
                    }

                });
        try {
            ChannelFuture bind = bootstrap.bind(new InetSocketAddress(16753));
            bind.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        System.out.println("绑定本地监听端口成功");
                    } else {
                        System.out.println("绑定本地监听端口失败！" + future.cause());
                    }
                }
            });
            bind.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            Future<?> future = eventLoopGroup.shutdownGracefully();
            future.syncUninterruptibly();
        }

    }

}
