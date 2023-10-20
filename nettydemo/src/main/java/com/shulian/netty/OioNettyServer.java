package com.shulian.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.local.LocalAddress;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalEventLoopGroup;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import static java.lang.System.getProperty;

/**
 * 用netty实现的 bio 服务端
 * 使用netty框架，从bio模式迁移到nio或者epoll等其他io模式，只会改动少量代码。相比于
 * 使用jdk提供的那一套网络api，从bio迁移到nio完全是两套不同的代码，顶级采用的事不同的接口
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2020-03-30 23:34
 * @since jdk1.8
 */
@Slf4j
public class OioNettyServer {

    public static void main(String[] args) throws InterruptedException {
        String classpath = getProperty("user.home");
        log.info("类路径={}", classpath);
        int port = 9899;
        ByteBuf byteBuf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(("Hi!\r\n"), Charset.forName("UTF-8")));
        //从oio协议改为nio只需要改动两行代码
//        EventLoopGroup eventLoopGroup = new OioEventLoopGroup();
        EventLoopGroup eventLoopGroup = new DefaultEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(eventLoopGroup)
//                    .channel(OioServerSocketChannel.class)
//                    .channel(NioServerSocketChannel.class)
                    //使用local-in VM模式，同一个JVM里面通信
                    .channel(LocalServerChannel.class)
//                    .localAddress(new InetSocketAddress(port))
                    .localAddress(new LocalAddress(classpath))
                    .childHandler(new ChannelInitializer<LocalServerChannel>() {

                        @Override
                        protected void initChannel(LocalServerChannel ch) throws Exception {
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    log.info("连接成功!!!!");
                                    ctx.writeAndFlush(byteBuf.duplicate())
                                            .addListener(ChannelFutureListener.CLOSE);
                                }
                            });
                        }
                    });
            ChannelFuture future = bootstrap.bind().sync();
            //当前线程会阻塞,直到未来这个通道关闭事件发生
            future.channel().closeFuture().sync();
        } finally {
            eventLoopGroup.shutdownGracefully().sync();
        }


    }
}
