package com.shulian.bus.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * topic exchange是RabbitMQ中最灵活的一种方式，可以根据binding_key自由的绑定不同的队列
 *
 * 首先对topic规则配置，这里使用两个队列来测试（也就是在Application类中创建和绑定的topic.message
 * 和topic.messages两个队列），其中topic.message的bindting_key为
 *
 * “topic.message”，topic.messages的binding_key为“topic.#”；
 * @author zhangjuwa
 * @description 主题交换机绑定策略
 * @date 2018/4/24
 * @since jdk1.8
 */
@Component
public class TopicSender {
    private final Logger logger = LoggerFactory.getLogger(TopicSender.class);
    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send() {
        String msg1 = "I am topic.mesaage msg======";
        logger.info("发送 消息1 ：" + msg1);
        rabbitTemplate.convertAndSend("topicExchange","topic.message", msg1);

        String msg2 = "I am topic.mesaages msg########";
        logger.info("发送 消息2 ：" + msg2);
        rabbitTemplate.convertAndSend("topicExchange","topic.messages", msg2);
    }
}
