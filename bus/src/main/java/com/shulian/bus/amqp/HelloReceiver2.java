package com.shulian.bus.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author zhangjuwa
 * @description 消费者2
 * @date 2018/4/23
 * @since jdk1.8
 */
@Component
@RabbitListener(queues = {"hello"})
public class HelloReceiver2 {

    private final Logger logger = LoggerFactory.getLogger(HelloReceiver2.class);

    @RabbitHandler
    public void process(String message) {
        logger.info("receive2 message : "+ message + Thread.currentThread().getName() + ": "+ new Date());
    }
}
