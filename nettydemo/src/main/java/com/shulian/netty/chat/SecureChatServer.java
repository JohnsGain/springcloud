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
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.util.concurrent.ImmediateEventExecutor;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.SSLException;
import java.net.InetSocketAddress;
import java.security.cert.CertificateException;

/**
 * 带SSL的聊天室启动服务
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2020-09-28 10:59
 * @since jdk1.8
 */
@Slf4j
public class SecureChatServer {

    private final ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);

    private final EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

    private Channel channel;

    public ChannelFuture start(InetSocketAddress address, SslContext sslContext) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(eventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(createInitializer(channelGroup, sslContext));
        ChannelFuture channelFuture = serverBootstrap.bind(address);
        channelFuture.syncUninterruptibly();
        log.info("chatServer绑定本地端口{}成功", address.getPort());
        channel = channelFuture.channel();
        return channelFuture;
    }


    public static void main(String[] args) {
        int port = 8778;
        SecureChatServer endpoint = new SecureChatServer();
        ChannelFuture channelFuture = null;
        try {
            SelfSignedCertificate certificate = new SelfSignedCertificate();
            SslContext context = SslContextBuilder.forServer(certificate.certificate(), certificate.privateKey()).build();
            channelFuture = endpoint.start(new InetSocketAddress(port), context);
        } catch (CertificateException e) {
            log.warn("获取签名证书异常", e);
        } catch (SSLException e) {
            log.warn("创建SSL上下文异常", e);
        }
        Runtime.getRuntime().addShutdownHook(new Thread(endpoint::destroy));
        if (channelFuture != null) {
            channelFuture.channel().closeFuture().syncUninterruptibly();
        }

    }

    private ChannelInitializer<Channel> createInitializer(ChannelGroup channelGroup, SslContext sslContext) {
        return new SecureChatServerInitializer(sslContext, channelGroup);
    }

    private void destroy() {
        if (channel != null) {
            channel.close();
        }
        channelGroup.close();
        eventLoopGroup.shutdownGracefully();
    }
}
