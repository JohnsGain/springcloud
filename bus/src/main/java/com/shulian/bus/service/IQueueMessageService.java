package com.shulian.bus.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @author ""
 * @description 队列服务
 * @date 2018/4/23
 * @since jdk1.8
 */
public interface IQueueMessageService extends RabbitTemplate.ConfirmCallback {

    /**
     * 发送消息到rabbitmq消息队列
     * @param message 消息内容
     * @param exchangeName 交换几名称
     * @param queueName 队列名称
     */
    void send(Object message, String exchangeName, String queueName);
}
