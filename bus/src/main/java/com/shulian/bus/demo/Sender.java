package com.shulian.bus.demo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ""
 * @description 模拟RabbitMQ生产者  producer,consumer,virtual host,broker,exchange,queue
 * exchange种类：
 * 1.direct exchange :直接匹配，通过交换机名称和Routing Key来接收和发送消息
 * 2.FanOut exchange :广播订阅，向所有消费者广播消息，但是只有消费者把队列绑定到该exchange上才会接受到消息，忽略Routing Key
 * 3.Topic exchange: 主题订阅，主题指Routing Key，Routing Key可以采用通配符{*}{#}等，Routing Key命名采用.分割多个词，
 * 但是只有消费者把队列绑定到该exchange上且指定的Routing Key和主题匹配才能接收到消息
 * 4.Header exchange: 消息头订阅，为消息定义一个或多个键值对的消息头,然后消费者接收消息同时需要定义类似的键值对
 * 请求头:(如:x-mactch=all或者x_match=any)，只有请求头与消息头匹配,才能接收消息,忽略RoutingKey.
 * 5.默认的exchange:如果用空字符串去声明一个exchange，那么系统就会使用”amq.direct”这个exchange，我们创建一个
 * queue时,默认的都会有一个和新建queue同名的routingKey绑定到这个默认的exchange上去
 * 生产者创建过程：
 * (1)：创建ConnectionFactory，并且设置一些参数，比如hostname,portNumber等等
 * <p>
 * (2)：利用ConnectionFactory创建一个Connection连接
 * <p>
 * (3)：利用Connection创建一个Channel通道
 * <p>
 * (4)：创建queue并且和Channel进行绑定
 * <p>
 * (5)：创建消息，并且发送到队列中
 * @date 2018/4/20
 * @since jdk1.8
 */
public class Sender {

    private static final Logger logger = LoggerFactory.getLogger(Sender.class);

    private static final String QUEUE_NAME = "order-queue";

    public static void main(String[] args) {
        try {
            send();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送消息，这里没有创建exchange,RabbitMQ默认会创建一个空字符串的exchange ,默认会使用这个exchange
     */
    private static void send() throws IOException, TimeoutException {
        ConnectionFactory factory = null;
        Connection connection = null;
        Channel channel = null;
        try {
            factory = new ConnectionFactory();
            factory.setHost("localhost");
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            logger.info("connection is : " + connection);
            String message = "my first message22233";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
            logger.info("已发送 :  " + message);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            if (connection != null) {
                channel.close();
                connection.close();
            }
        }
    }
}
