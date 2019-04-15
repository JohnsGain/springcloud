package com.shulian.bus.service.impl;

import com.shulian.bus.amqp.HelloReceiver2;
import com.shulian.bus.service.IQueueMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author ""
 * @description 队列服务实现
 * @date 2018/4/23
 * @since jdk1.8
 */
@Service
public class QueueMessageServiceImpl implements IQueueMessageService {

    private final Logger logger = LoggerFactory.getLogger(QueueMessageServiceImpl.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void send(Object message, String exchangeName, String queueName) {
        //设置回调对象为当前对象
        rabbitTemplate.setConfirmCallback(this);

        //构建回调ID为uuid
        CorrelationData data = new CorrelationData(UUID.randomUUID().toString().replaceAll("-", ""));

        //发送消息到消息队列
        rabbitTemplate.convertAndSend(exchangeName, queueName, message, data);
    }

    /**
     * 消息回调确认方法
     * @param correlationData 请求数据对象
     * @param b 是否发送成功
     * @param s
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        logger.info("毁掉id： "+ correlationData.getId());

        if (b) {
            logger.info("消息发送成功");
        } else {
            logger.info("消息发送失败:  " + s);
        }
    }
}
