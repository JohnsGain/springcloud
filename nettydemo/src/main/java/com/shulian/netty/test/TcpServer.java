package com.shulian.netty.test;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

/**
 * 模拟启动tcp服务
 *
 * @author Lee HN
 * @date 2019/6/30 1:00
 */
@Slf4j
public class TcpServer {

    ChannelHandler channelInit;

    @Before
    public void befor() {
        channelInit = new ChannelInit();
    }

    @Test
    public void init() throws InterruptedException {
        String strategy = "nio";
        EventLoopGroup boss = ChannelFactoryImpl.createEventLoopGroup(strategy);
        EventLoopGroup worker = ChannelFactoryImpl.createEventLoopGroup(strategy, 10);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker)
                    .channel(ChannelFactoryImpl.getServerSocketChannel(strategy))
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(channelInit);
            int tcpPort = 9111;
            ChannelFuture future = bootstrap.bind(tcpPort).sync();

            log.info("netty socketServer started on port(s) " + tcpPort + " (tcp)");
            future.channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
