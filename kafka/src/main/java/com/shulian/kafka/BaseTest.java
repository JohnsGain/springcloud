package com.shulian.kafka;

import com.google.common.collect.Lists;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;

/**
 * @author zhangjuwa
 * @description
 * @date 2018/9/25
 * @since jdk1.8
 */
public abstract class BaseTest {

    protected static RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();

    protected static String topic = "test";

    public final static void init() {
        RedisClusterConfiguration clusterConfiguration = new RedisClusterConfiguration(Lists.newArrayList("DDBtest008:6379"));
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory(clusterConfiguration);
        connectionFactory.afterPropertiesSet();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.afterPropertiesSet();
        //启用事务支持
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.setKeySerializer(redisTemplate.getStringSerializer());
    }

}
