package com.shulian.netty.test;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.kqueue.KQueueDatagramChannel;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.kqueue.KQueueServerSocketChannel;
import io.netty.channel.kqueue.KQueueSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.socket.oio.OioDatagramChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;
import io.netty.channel.socket.oio.OioSocketChannel;

/**
 * @author Lee HN
 * @date 2019/5/25 14:49
 */
public class ChannelFactoryImpl {

    public static final EventLoopGroup createEventLoopGroup(String name) {
        return createEventLoopGroup(name, 0);
    }

    public static final EventLoopGroup createEventLoopGroup(String name, int nThreads) {
        if (name == null) return null;
        switch (name) {
            case "nio":
                return new NioEventLoopGroup(nThreads);
            case "epoll":
                return new EpollEventLoopGroup(nThreads);
            case "kqueue":
                return new KQueueEventLoopGroup(nThreads);
            default:
                return new OioEventLoopGroup(nThreads);
        }
    }

    public static final Class<? extends ServerSocketChannel> getServerSocketChannel(String name) {
        if (name == null) return null;
        switch (name) {
            case "nio":
                return NioServerSocketChannel.class;
            case "epoll":
                return EpollServerSocketChannel.class;
            case "kqueue":
                return KQueueServerSocketChannel.class;
            default:
                return OioServerSocketChannel.class;
        }
    }

    public static final Class<? extends SocketChannel> getSocketChannel(String name) {
        if (name == null) return null;
        switch (name) {
            case "nio":
                return NioSocketChannel.class;
            case "epoll":
                return EpollSocketChannel.class;
            case "kqueue":
                return KQueueSocketChannel.class;
            default:
                return OioSocketChannel.class;
        }
    }

    public static final Class<? extends DatagramChannel> getDatagramChannel(String name) {
        if (name == null) return null;
        switch (name) {
            case "nio":
                return NioDatagramChannel.class;
            case "epoll":
                return EpollDatagramChannel.class;
            case "kqueue":
                return KQueueDatagramChannel.class;
            default:
                return OioDatagramChannel.class;
        }
    }
}
