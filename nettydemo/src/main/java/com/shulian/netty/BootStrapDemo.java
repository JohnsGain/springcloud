package com.shulian.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.socket.oio.OioSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * The Bootstrap class is responsible for creating channels for clients
 * and for applications that utilize connectionless protocols,
 *
 * nettyt的启动类要注意主件适配，NioEventLoopGroup就要使用 NioSocketChannel；OioEventLoopGroup就要使用OioSocketChannel,
 * 混合使用会抛出 IllegalStateException, because it mixes incompatible  transports:
 * @author zhangjuwa
 * @apiNote
 * @date 2020-09-11 08:18
 * @since jdk1.8
 */
public class BootStrapDemo {

    /**
     * @param args
     */
    public static void main(String[] args) {
        EventLoopGroup eventExecutors = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventExecutors)
                .channel(NioSocketChannel.class)
                .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                        System.out.println("接收到数据=" + msg.toString(CharsetUtil.UTF_8));
                    }
                })
                .remoteAddress(new InetSocketAddress("www.baidu.com", 80));

        try {
            ChannelFuture sync = bootstrap.connect().sync();
            sync.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        System.out.println("连接成功");
                    } else {
                        System.out.println("连接失败: " + future.cause());
                    }
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


}
