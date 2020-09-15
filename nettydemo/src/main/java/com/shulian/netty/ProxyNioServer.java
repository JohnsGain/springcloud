package com.shulian.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * @author zhangjuwa
 * @apiNote Suppose your server is processing a client request that requires it to act as a client to
 * a third system. This can happen when an application, such as a proxy server, has to
 * integrate with an organization’s existing systems, such as web services or databases. In
 * such cases you’ll need to bootstrap a client Channel from a ServerChannel.
 * <p>
 * 译文：假如你的Netty正在处理一个客户端请求，需要netty服务端扮演一个 访问第三方系统的客户端。这种情况可能发生在诸如代理服务器之类的应用上，
 * 这种代理服务器需要和 第三方组织机构已经存在的系统(比如web应用或数据库)交互。
 * 这就需要你在netty服务端接收的channel 里面启动一个 netty客户端
 * @date 2020-09-14 16:42
 * @since jdk1.8
 */
public class ProxyNioServer {

    public static void main(String[] args) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(new NioEventLoopGroup(), new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                    ChannelFuture connectFuture;

                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        Bootstrap bootstrap = new Bootstrap();
                        bootstrap.group(ctx.channel().eventLoop())
                                .channel(NioSocketChannel.class)
                                .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                                        System.out.println("接收到第三方服务的数据 ：" + msg.toString(CharsetUtil.UTF_8));
                                    }
                                })
                                .remoteAddress(new InetSocketAddress("www.baidu.com", 80));
                        connectFuture = bootstrap.connect();

                    }

                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                        if (connectFuture.isDone()) {
                            System.out.println("do  something...");
                        }
                    }
                })
                .localAddress(new InetSocketAddress(8899));
        ChannelFuture sync = serverBootstrap.bind();
        sync.addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                System.out.println("绑定本地监听端口成功");
            } else {
                System.out.println("绑定本地监听端口失败！" + future.cause());
            }
        });

    }

}
