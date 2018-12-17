package com.shulian.bus.demo;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消费者创建过程
 * (1)：创建ConnectionFactory，并且设置一些参数，比如hostname,portNumber等等
 *
 *      (2)：利用ConnectionFactory创建一个Connection连接
 *
 *      (3)：利用Connection创建一个Channel通道
 *
 *      (4)：将queue和Channel进行绑定，注意这里的queue名字要和前面producer创建的queue一致
 *
 *      (5)：创建消费者Consumer来接收消息，同时将消费者和queue进行绑定
 * @author zhangjuwa
 * @description 创建RabbitMq消费者
 * @date 2018/4/20
 * @since jdk1.8
 */
public class Receiver {

    private final static String QUEUE_NAME = "order-queue";

    private static final Logger logger = LoggerFactory.getLogger(Receiver.class);

    public static void main(String[] args) {
        try {
            receive();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    /**
     * 消费者接受消息
     */
    private static void receive() throws IOException, TimeoutException {
        ConnectionFactory factory = null;
        Connection connection = null;
        Channel channel = null;
        try {
            factory = new ConnectionFactory();
            factory.setHost("localhost");
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, "UTF-8");
                    logger.info("接收到消息： "+ message);
                }
            };
            String result = channel.basicConsume(QUEUE_NAME, true, consumer);
            System.out.println(result);
            channel.basicConsume(QUEUE_NAME, true, consumer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (channel != null) {
                channel.close();
            }
            if (connection != null) {
                connection.close();
            }

        }
    }
}
