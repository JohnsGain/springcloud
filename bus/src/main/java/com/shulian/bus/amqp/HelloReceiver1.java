package com.shulian.bus.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * 消息接收者是一个简单的POJO类，它定义了一个方法去接收消息，当你注册它去接收消息，
 * 你可以给它取任何的名字。其中，它有CountDownLatch这样的一个类，它是用于告诉发送者消息已经收到了，
 * 你不需要在应用程序中具体实现它，只需要latch.countDown()就行了。
 * @author zhangjuwa
 *
 * @description 消费者1
 * @date 2018/4/23
 * @since jdk1.8
 */
@Component
//@RabbitListener(queues = {"hello"})
public class HelloReceiver1 {

    /**
     * 用于告诉发送者消息已经收到了，
     *  你不需要在应用程序中具体实现它，只需要latch.countDown()就行了
     */
    private CountDownLatch latch = new CountDownLatch(1);

    private final Logger logger = LoggerFactory.getLogger(HelloSender1.class);

    public void process(String message) {
        logger.info("receive1 message : "+ message + Thread.currentThread().getName());
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }
}
