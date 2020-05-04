package com.shulian.netty.chapter8;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * 通过把自动聚合器添加到pipeline里面，让用户可以方便地获取到完整的httprequest和httpresponse消息，
 * 而不用担心得到http消息片段。
 *
 *  netty还提供了数据的解压缩：
 *   The performance gain from this isn’t free, as compression puts more
 * load on the CPU. Although today’s increased computing capabilities mean that this isn’t as
 * much of an issue as a few years ago, using compression is a good idea most of the time if you
 * transfer content that benefits from compression like text.
 * @author zhangjuwa
 * @apiNote
 * @date 2020-04-13 22:55
 * @since jdk1.8
 */
public class HttpAggregatorInitializer extends ChannelInitializer<Channel> {

    private final boolean client;

    public HttpAggregatorInitializer(boolean client) {
        this.client = client;
    }


    /**
     * As you can see, it’s easy to let Netty automatically aggregate the message parts for you. Be
     * aware that to guard your server against DoS attacks you need to choose a sane limit for the
     * maximum message size, that is configured by the constructor of HttpObjectAggregator. How
     * big the maximum message size should be depends on your use case, concurrent
     * requests/responses, and, of course, the amount of usable memory available.
     * @param ch
     * @throws Exception
     */
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (client) {
            pipeline.addLast("codec", new HttpClientCodec());
        } else {
            pipeline.addLast("codec", new HttpServerCodec());
        }
//        Add HttpObjectAggregator to the ChannelPipeline, using a max message size of 512kb. After the
//        message is getting bigger a TooLongFrameException is thrown.
        pipeline.addLast("aggregator", new HttpObjectAggregator(512 * 1024));

        pipeline.addLast("decompressor", new HttpContentCompressor());
    }

}
