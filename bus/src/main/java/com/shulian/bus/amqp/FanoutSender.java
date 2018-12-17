package com.shulian.bus.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Fanout 就是我们熟悉的广播模式或者订阅模式，给Fanout转发器发送消息，绑定了这个转发器的所有队列都收到这个消息
 * @author zhangjuwa
 * @description 广播模式的交换机
 * @date 2018/4/24
 * @since jdk1.8
 */
@Component
public class FanoutSender {
    private final Logger logger = LoggerFactory.getLogger(FanoutSender.class);

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send() {
        String msgString="fanoutSender :hello i am fanout exchange message";
        logger.info("发送消息 : "+ msgString);
        rabbitTemplate.convertAndSend("fanoutExchange", "abc.de", msgString);
    }
}
