package com.shulian.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;

/**
 * This method provides an easy way to add multiple ChannelHandlers to a ChannelPipeline.
 * You simply provide your implementation of ChannelInitializer to the
 * bootstrap, and once the Channel is registered with its EventLoop your version of initChannel()
 * is called. After the method returns, the ChannelInitializer instance
 * removes itself from the ChannelPipeline.
 * @author zhangjuwa
 * @apiNote
 * @date 2020-09-15 11:30
 * @since jdk1.8
 */
public class ChannelInitializerImpl extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpClientCodec())
                .addLast(new HttpObjectAggregator(Integer.MAX_VALUE));
    }
}
