package com.shulian.netty.channelinitializer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * 用于对Inbound和Outbound数据进行SSL/TLS加密或解密，通过 ChannelInitializer 把这个Sslhandler加入到channelpipeline
 *
 * One important thing to note is that in almost all cases the SslHandler must be the first
 * ChannelHandler in the ChannelPipeline. There may be some exceptions, but take this as
 * rule of thumb. Recall that in chapter 6, we said the ChannelPipeline is like a LIFO13 (last-infirst-out)
 * queue for inbound messages and a FIFO14 (first-in-first-out) queue for outbound
 * messages. Adding the SslHandler first ensures that all other ChannelHandlers have applied
 * their transformations/logic to the data before it’s encrypted, thus ensuring that changes from
 * all handlers are secured on a Netty server.
 *
 * 这个类 shows how an SslHandler is added to a ChannelPipeline using a ChannelInitializer.
 * Recall that ChannelInitializer is used to set up the ChannelPipeline
 * once a Channel is registered
 *
 * In most cases the SslHandler will be the first ChannelHandler in the ChannelPipeline.
 * This ensures that encryption will take place only after all other ChannelHandlers have
 * applied their logic to the data.
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2020-04-13 20:05
 * @since jdk1.8
 */
public class SslChannelInitializer extends ChannelInitializer<Channel> {

    //    private final SSLContext context;
    private final SslContext context;
    private final boolean client;
    private final boolean startTls;

    public SslChannelInitializer(SslContext context, boolean client, boolean startTls) {
        this.context = context;
        this.client = client;
        this.startTls = startTls;
    }


    /**
     * @param ch 把sslHandler放入第一个handler，确保对于Inbound data,其他inboundchannelHandler处理之前数据
     *           已经被解密；以及对于outbound data,在被所有outboundHandler处理之后，写到客户端之前，能在最后把整个数据加密
     * @throws Exception
     */
    @Override
    protected void initChannel(Channel ch) throws Exception {
//        SSLEngine sslEngine = context.createSSLEngine();
        SSLEngine sslEngine = context.newEngine(ch.alloc());
        sslEngine.setUseClientMode(client);
        ch.pipeline().addFirst("ssl",new SslHandler(sslEngine, startTls));
    }

}
