package com.shulian.bus.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 模拟多生产者多消费者
 * @author zhangjuwa
 * @description 生产者2
 * @date 2018/4/23
 * @since jdk1.8
 */
@Component
public class HelloSender2 {

    private final Logger logger = LoggerFactory.getLogger(HelloSender2.class);
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(String msg) {
        String message = msg + new Date();
        logger.info("发送消息： "+ message);
        rabbitTemplate.convertAndSend("hello", message);
    }
}
