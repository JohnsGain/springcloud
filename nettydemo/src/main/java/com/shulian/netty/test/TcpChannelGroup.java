package com.shulian.netty.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Lee HN
 * @date 2019/6/19 15:04
 */
@Slf4j
public class TcpChannelGroup {

    private ObjectMapper objectMapper;

    private final Map<Object, Channel> channelMap;

    public TcpChannelGroup(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.channelMap = new ConcurrentHashMap<>();
    }

    @Async
    public void send(Object channelKey, Object message) throws IOException {
        String msg = message instanceof String ? (String) message : objectMapper.writeValueAsString(message);
        Channel channel = channelMap.get(channelKey);
        if (channel != null) channel.writeAndFlush(msg);
        else log.warn("用户 " + channelKey + " socket未连接");
    }

    public Channel find(Object channelKey) {
        return channelMap.get(channelKey);
    }

    public Map<Object, Channel> getAll() {
        return channelMap;
    }

    public void add(Object channelKey, Channel channel) {
        Channel oldChannel = channelMap.put(channelKey, channel);
        if (oldChannel != null) oldChannel.close();
    }

    public boolean remove(Channel channel) {
        Object channelKey = channel.attr(AttributeKey.valueOf("id")).get();
        return channelKey != null && channelMap.remove(channelKey, channel);
    }
}
