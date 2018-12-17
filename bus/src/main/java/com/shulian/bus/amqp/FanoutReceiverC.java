package com.shulian.bus.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author zhangjuwa
 * @description 订阅模式消费者1
 * @date 2018/4/24
 * @since jdk1.8
 */
@Component
@RabbitListener(queues = {"fanout.C"})
public class FanoutReceiverC {

    private final Logger logger = LoggerFactory.getLogger(FanoutReceiverC.class);

    @RabbitHandler
    public void process(String message) {
        logger.info("FanoutReceiverC message : " + message);
    }
}
