package com.shulian.netty.channelinitializer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;

/**
 * The class HttpPipelineInitializer in the next listing shows how simple it is to add
 * HTTP support to your application—merely add the correct ChannelHandlers to the
 * ChannelPipeline.
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2020-09-18 19:35
 * @since jdk1.8
 */
public class HttpPipelineInitializer extends ChannelInitializer<Channel> {

    private final boolean client;

    public HttpPipelineInitializer(boolean client) {
        this.client = client;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (client) {
            pipeline.addLast("encoder", new HttpRequestEncoder());
            pipeline.addLast("decoder", new HttpResponseDecoder());
        } else {
            pipeline.addLast("encoder", new HttpRequestEncoder());
            pipeline.addLast("decoder", new HttpResponseDecoder());
        }
    }
}
