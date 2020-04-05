package com.shulian.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.local.LocalAddress;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalServerChannel;
import lombok.extern.slf4j.Slf4j;

import static java.lang.System.getProperty;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2020-03-24 18:05
 * @since jdk1.8
 */
@Slf4j
public class EchoClient {

    private final String host;
    private final int port;

    private EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        new EchoClient("192.168.124.5", 8777).start();
    }

    private void start() throws InterruptedException {
        String classpath = getProperty("user.home");
        log.info("类路径={}", classpath);
//        EventLoopGroup group = new NioEventLoopGroup();
        EventLoopGroup group = new DefaultEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
//                    .channel(NioSocketChannel.class)
                    .channel(LocalChannel.class)//使用local-in VM模式，同一个JVM里面通信
//                    .remoteAddress(new InetSocketAddress(host, port))
                    .remoteAddress(new LocalAddress(classpath))
                    .handler(new ChannelInitializer<LocalChannel>() {
                        @Override
                        protected void initChannel(LocalChannel ch) throws Exception {
//                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            ChannelFuture sync = bootstrap.connect().sync();
            sync.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}
