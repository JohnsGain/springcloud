package com.shulian.netty.chapter8;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

/**
 * As mentioned before in this chapter, you may want to protect your network traffic using
 * encryption. You can do this via HTTPS, which is a piece of cake thanks to the “stackable”
 * ChannelHandlers that come with Netty.
 * All you need to do is to add the SslHandler to the mix,
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2020-04-14 00:03
 * @since jdk1.8
 */
public class HttpsCodecInitializer extends ChannelInitializer<Channel> {

    private final boolean client;


    private final SSLContext sslContext;

    public HttpsCodecInitializer(boolean client, SSLContext sslContext) {
        this.client = client;
        this.sslContext = sslContext;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        SSLEngine sslEngine = sslContext.createSSLEngine();
        pipeline.addFirst("ssl",new SslHandler(sslEngine));
        if (client) {
            pipeline.addLast("codec", new HttpClientCodec());
        } else {
            pipeline.addLast("coder", new HttpServerCodec());
        }

    }
}
