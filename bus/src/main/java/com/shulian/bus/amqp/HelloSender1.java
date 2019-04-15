package com.shulian.bus.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 最简单的hello生产和消费实现（单生产者和单消费者）
 * @author ""
 * @description 消息生产者
 * @date 2018/4/23
 * @since jdk1.8
 */
@Component
public class HelloSender1 {

    private final Logger logger = LoggerFactory.getLogger(HelloSender1.class);

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send(String msg) {
        String message = msg + new Date();
        logger.info("send message :  " + message);
        this.rabbitTemplate.convertAndSend("hello", message);
    }


}
