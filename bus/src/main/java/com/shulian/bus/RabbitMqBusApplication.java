package com.shulian.bus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Spring Cloud Bus 将分布式的节点用轻量的消息代理连接起来。它可以用于广播配置文件的更改或者服务之间的通讯，
 * 也可以用于监控。本文要讲述的是用Spring Cloud Bus实现通知微服务架构的配置文件的更改。
 * @author ""
 * @description 用RabbitMQ实现的事件总线
 * @date 2018/4/20
 * @since jdk1.8
 */
@SpringBootApplication
@EnableDiscoveryClient
public class RabbitMqBusApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitMqBusApplication.class, args);
    }
}
