package com.shulian.bus.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhangjuwa
 * @description 用户消息生产者
 * @date 2018/4/24
 * @since jdk1.8
 */
@Component
public class UserSender {

    private final Logger logger = LoggerFactory.getLogger(UserSender.class);

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void send(User user) {
       /* User user = new User();
        user.setName("john ");
        user.setPass("1245");*/
        logger.info("发送用户消息： "+ user);
        amqpTemplate.convertAndSend("user", user);
    }
}
