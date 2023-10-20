package com.shulian.netty.channelinitializer;

import com.shulian.netty.handler.HeartbeatHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * 限制连接和超时处理
 * The most used in practice is the IdleStateHandler, so let’s focus on it.
 * Listing 8.9 shows how you can use the IdleStateHandler to get notified if you haven’t
 * received or sent data for 60 seconds. If this is the case, a heartbeat will be written to the
 * remote peer, and if this fails the connection is closed
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2020-04-14 12:22
 * @since jdk1.8
 */
public class IdleStateHandlerInitializer extends ChannelInitializer<Channel> {

    /**
     * @param ch The most used in practice is the IdleStateHandler, so let’s focus on it.
     *           * Listing 8.9 shows how you can use the IdleStateHandler to get notified if you haven’t
     *           * received or sent data for 60 seconds. If this is the case, a heartbeat will be written to the
     *           * remote peer, and  if there is no response the connection is closed.
     * @throws Exception
     */
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new IdleStateHandler(0, 0, 60, TimeUnit.SECONDS),
                new HeartbeatHandler());
    }

}
