package com.shulian.bus.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author ""
 * @description 接受用户消息的消费者
 * @date 2018/4/24
 * @since jdk1.8
 */
@Component
@RabbitListener(queues = {"user"})
public class UserReceiver {

    private final Logger logger = LoggerFactory.getLogger(UserReceiver.class);
    @RabbitHandler
    public void process(User user) {
        logger.info("接收到用户消息 ：" + user  );
    }
}
