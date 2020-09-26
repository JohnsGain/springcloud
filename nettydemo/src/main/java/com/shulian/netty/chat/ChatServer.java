package com.shulian.netty.chat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.ImmediateEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * The final piece of the picture is the code that bootstraps the server and installs the
 * ChatServerInitializer. This will be handled by the ChatServer class, as shown here
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2020-09-25 23:44
 * @since jdk1.8
 */
@Slf4j
public class ChatServer {

    private final ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);

    private final EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

    private Channel channel;

    public ChannelFuture start(InetSocketAddress address) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(eventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(createInitializer(channelGroup));
        ChannelFuture channelFuture = serverBootstrap.bind(address);
        channelFuture.syncUninterruptibly();
        log.info("chatServer绑定本地端口{}成功", address.getPort());
        channel = channelFuture.channel();
        return channelFuture;
    }


    public static void main(String[] args) {
        int port = 8778;
        ChatServer endpoint = new ChatServer();
        ChannelFuture channelFuture = endpoint.start(new InetSocketAddress(port));
        Runtime.getRuntime().addShutdownHook(new Thread(endpoint::destroy));
        channelFuture.channel().closeFuture()
                .syncUninterruptibly();

    }

    private ChannelInitializer<Channel> createInitializer(ChannelGroup channelGroup) {
        return new ChatServerInitializer(channelGroup);
    }

    private void destroy() {
        if (channel != null) {
            channel.close();
        }
        channelGroup.close();
        eventLoopGroup.shutdownGracefully();
    }

}
