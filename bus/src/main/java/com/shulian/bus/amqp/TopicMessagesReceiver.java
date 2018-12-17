package com.shulian.bus.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author zhangjuwa
 * @description 接受匹配bindingKey为topic.#的消息
 * @date 2018/4/24
 * @since jdk1.8
 */
@Component
@RabbitListener(queues = {"topic.messages"})
public class TopicMessagesReceiver {

    private final Logger logger = LoggerFactory.getLogger(TopicMessagesReceiver.class);

    @RabbitHandler
    public void process(String msg) {
        logger.info("topicMessagesReceiver : " + msg);
    }
}
