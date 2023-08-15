package eureka.config;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;

/**
 * 当同一主题下有新的消费者上线，kafka服务收到上线信息后会对改主题下各个消费者进行重新分区分配
 *  这个时候这个监听就会启动
 * @see {官网关于该监听器的使用案例 http://kafka.apache.org/0100/javadoc/org/apache/kafka/clients/consumer/ConsumerRebalanceListener.html}
 * @author ""
 * @description
 * @date 2018/9/28
 * @since jdk1.8
 */
//@Component
public class ConsumerRebalanceListenerImpl implements ConsumerRebalanceListener {

    private final Logger logger = LoggerFactory.getLogger(ConsumerRebalanceListenerImpl.class);

    @Autowired
    private Consumer consumer;

    private RedisTemplate<String, Serializable> redisTemplate;

    /**
     * 初始化方法，传入consumer对象，否则无法调用外部的consumer对象，必须传入
     *
     * @param consumer
     */
    public ConsumerRebalanceListenerImpl(Consumer consumer, RedisTemplate<String, Serializable> redisTemplate) {
        this.consumer = consumer;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 在分区重分配之前执行，在这里保存当前分区的偏移量
     *
     * @param partitions
     */
    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
        logger.info("分区重分配之前");
        //for (TopicPartition partition : partitions) {
        //    redisTemplate.opsForValue().set(topic + "_" + partition.partition(), this.consumer.position(partition));

        //}
        //这里在程序处理的时候就已经保存了当前客户端具体分区的偏移量在数据库，所以这里不做处理
    }

    /**
     * 完成分区重分配之后执行，定位到最新分配分区的偏移量
     * 重新分配分区之后，可能当前消费者客户端分区有变动，增加或减少，都需要通过外部数据库读取保存
     * 好的各分区偏移量，这样能避免错误的重复消费，那种虽然消费了，但是每天保存消费结果
     * 的重复没有问题
     *
     * @param partitions
     */
    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
        logger.info(" 完成分区重分配之后");
        for (TopicPartition partition : partitions) {
            Long offSet = (Long) redisTemplate.opsForValue().get(partition.topic() + "_" + partition.partition());
            //如果为null,说明是最新开始的消费
            if (offSet != null) {
                this.consumer.seek(partition, offSet);
            }
        }
    }
}
