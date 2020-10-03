package com.shulian.netty.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;

/**
 * Now we need to install our handlers in the ChannelPipeline, as seen in figure 14.4.
 * This listing shows how it is done by the LogEventMonitorClient main class.
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2020-10-03 18:00
 * @since jdk1.8
 */
public class LogEventMonitorClient {

    private final EventLoopGroup group;
    private final Bootstrap bootstrap;

    public LogEventMonitorClient(InetSocketAddress address) {
        this.group = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new LogEventDecoder())
                                .addLast(new LogEventHandler());
                    }
                })
                .localAddress(address);
    }

    public static void main(String[] args) {
        LogEventMonitorClient monitorClient = new LogEventMonitorClient(new InetSocketAddress(9999));
        try {
            Channel bind = monitorClient.bind();
            System.out.println("LogEventMonitor running");
            bind.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            monitorClient.stop();
        }

    }

    public void stop() {
        group.shutdownGracefully();
    }

    public Channel bind() throws InterruptedException {
        return bootstrap.bind().sync().channel();
    }

}
