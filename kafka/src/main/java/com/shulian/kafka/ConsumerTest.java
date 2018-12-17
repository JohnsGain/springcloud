package com.shulian.kafka;

import com.google.common.collect.Lists;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.*;

/**
 * @author zhangjuwa
 * @description kafka消费者端测试
 * @date 2018/9/20
 * @see {官方文档 https://kafka.apache.org/0102/javadoc/org/apache/kafka/clients/consumer/KafkaConsumer.html}
 * @since jdk1.8
 */
public class ConsumerTest extends BaseTest {

    static Logger logger = LoggerFactory.getLogger(ConsumerTest.class);

    public static void main(String[] args) {
        init();
        //自动提交消费消息的偏移量
        //autoCommit();

        //手动提交消费消息的偏移量
        manualCommit();

        //精确控制每个分区读取数据的偏移量提交位置
        //accurateOffset();

        //手动分配要消费的分区
        //manualPartitionAssign();

    }

    private static Properties getConfig(boolean autoCommit) {
        Properties props = new Properties();
        //设置要连接的broker,多个可以使用逗号隔开
        props.put("bootstrap.servers", "192.168.2.111:9092");
        //设置消费者组，同一个组的消费者可以通过负载均衡来消费消息，每个消费者负责消费主题的一个分区，
        //多余主题分区的消费者将不会被分配消息消费
        props.put("group.id", "test");
        //设置enable.auto.commit为true开启自动提交偏移,默认情况下是true
        props.put("enable.auto.commit", autoCommit ? "true" : "false");
        //自动提交的频率由 auto.commit.interval.ms
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        return props;
    }

    /**
     * 自动提交偏移，提交了说明消息是否已被消费,
     */
    public static void autoCommit() {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(getConfig(true));
        // 消费者订阅主题，一个消费者可以订阅多个主题。
        consumer.subscribe(Lists.newArrayList("test"), new ConsumerRebalanceListenerImpl(consumer));
        // 设置最大阻塞时间间隔
        Duration duration = Duration.ofSeconds(1L, 100L);
        while (true) {
            ConsumerRecords<String, String> consumerRecord = consumer.poll(duration);
            for (ConsumerRecord<String, String> record : consumerRecord) {
                logger.info("获取到消息: {}", record);
            }
        }
    }

    /**
     * 用户也可以手动控制提交偏移来决定消息是否已被消费。当消息需要经过一些特殊逻辑进行处理时，
     * 手动提交就非常有必要，没有经过处理的消息不应该当成已消费。
     * 当时间超过max.poll.interval.ms设置的值还未提交，就会默认当前消费者已经下线，会当前消费者分区的消息让给其他同组消费者消费
     */
    public static void manualCommit() {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(getConfig(false));
        consumer.subscribe(Lists.newArrayList(topic), new ConsumerRebalanceListenerImpl(consumer));

        final int minBatchSize = 10;
        List<ConsumerRecord<String, String>> buffer = new ArrayList<>();
        int count = 0;
        while (true) {
            ConsumerRecords<String, String> poll = consumer.poll(Duration.ofSeconds(1, 300));
            for (ConsumerRecord<String, String> record : poll) {
                buffer.add(record);
            }
            if (buffer.size() > minBatchSize) {
                //逻辑处理，例如保存到数据库
                for (ConsumerRecord<String, String> item : buffer) {
                    //if (count >= 50) {
                    //    consumer.close();
                    //    throw new RuntimeException("测试重复消费情况");
                    //}
                    logger.info("消息入库,主题:{}, 偏移量:{}, 消息内容:{},已消费{}", item.partition(), item.offset(), item.value(), count);
                    redisTemplate.opsForValue().set(topic + "_" + item.partition(), item.offset() + 1);
                    //开启事务之后这样提交，当前集群环境不支持事务操作MUTLI is currently not supported in cluster mode.
                    //redisTemplate.execute(new SessionCallback<List<Object>>() {
                    //    @Override
                    //    public List<Object> execute(RedisOperations operations) throws DataAccessException {
                    //        operations.multi();
                    //        operations.opsForValue().set(topic + "_" + item.partition(), item.offset() + 1);
                    //        //// This will contain the results of all ops in the transaction
                    //        return operations.exec();
                    //    }
                    //});
                    count ++;
                }

                consumer.commitAsync();
                //也可以提交之后执行回调,获取是否有异常，或者其他信息
                /*consumer.commitAsync((offsets, exception) -> {
                    if (exception != null) {
                        logger.info("提交发送异常{}", exception);
                    }
                });*/
                buffer.clear();
            }
        }
    }

    /**
     * 在某些情况下，您可能希望通过明确指定偏移量来更精确地控制已经提交的记录。
     * 在下面的例子中，我们在完成处理每个分区中的记录之后提交偏移量。
     * Note: The committed offset should always be the offset of the next message that your application will read.
     * Thus, when calling commitSync(offsets) you should add one to the offset of the last message processed.
     */
    private static void accurateOffset() throws InterruptedException {
        Properties config = getConfig(false);
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(config);
        consumer.subscribe(Lists.newArrayList(topic));
        try {
            while (true) {
                ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofSeconds(1, 3000));
                //获取当前消费者被分配的分区
                Set<TopicPartition> partitions = consumerRecords.partitions();
                for (TopicPartition partition : partitions) {
                    //获取当前消费者每个分区各自消费的消息
                    List<ConsumerRecord<String, String>> partitionRecodes = consumerRecords.records(partition);
                    for (ConsumerRecord<String, String> record : partitionRecodes) {
                        logger.info("主题:{}, 偏移量:{}, 消息内容:{}", record.partition(), record.offset(), record.value());
                    }
                    //获取当前分区最后一条消费的消息
                    ConsumerRecord<String, String> consumerRecord = partitionRecodes.get(partitionRecodes.size() - 1);
                    //获取最后这条消息的偏移量
                    long offset = consumerRecord.offset();
                    //在处理完每个分区消息之后获取偏移量位置精确控制提交
                    consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(offset + 1)));
                }
            }
        } finally {
            Thread.sleep(1000);
            //consumer.close();
        }

    }

    /**
     * 手动分配消费的分区，手动分配的分区将不会和由Kafka均衡自动分区协调分配，意味着当其他某个消费者下线
     * 之后，手动分配分区的客户端将不会重新参与分配分区，
     *
     * @see {https://kafka.apache.org/0102/javadoc/org/apache/kafka/clients/consumer/KafkaConsumer.html}
     * In the previous examples, we subscribed to the topics we were interested in and let Kafka dynamically
     * assign a fair share of the partitions for those topics based on the active consumers in the group.
     * However, in some cases you may need finer control over the specific partitions that are assigned.
     * For example:
     */
    private static void manualPartitionAssign() {
        TopicPartition topicPartition = new TopicPartition(topic, 0);
        TopicPartition topicPartition1 = new TopicPartition(topic, 1);
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(getConfig(true));
        consumer.assign(Lists.newArrayList(topicPartition, topicPartition1));

        while (true) {
            ConsumerRecords<String, String> poll = consumer.poll(Duration.ofSeconds(1, 3000));
            //重新定位主题某分区下一条即将消费的消息的偏移量
            //consumer.seek(new TopicPartition(topic, 1), 5L);
            //跳到主题分区下kafka服务最新的那条消息的位置
            //consumer.seekToEnd(Lists.newArrayList(topicPartition));
            //跳到主题分区下kafka服务保留得最老的那条消息位置
            //consumer.seekToBeginning(Lists.newArrayList(topicPartition1));

            //获取主题某分区下一条即将消费的消息偏移量
            //consumer.position(new TopicPartition(topic, 1))
            //获取当前消费者被分配的分区
            Set<TopicPartition> assignment = consumer.assignment();
            for (TopicPartition partition : assignment) {
                List<ConsumerRecord<String, String>> records = poll.records(partition);
                for (ConsumerRecord<String, String> record : records) {
                    logger.info("分区:{}, 偏移量:{}, 消息内容:{}", record.partition(), record.offset(), record.value());
                }
            }
        }
    }

    /**
     * 订阅主题的时候，同时注册分区重分配事件监听,当发生分区重新分配的时候，
     * 注册事件被触发{@link ConsumerRebalanceListener}
     */
    private static void consumeWithListener() {
        String topic = "topic";
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(getConfig(false));
        consumer.subscribe(Lists.newArrayList(topic), new ConsumerRebalanceListenerImpl(consumer));
        while (true) {
            ConsumerRecords<String, String> poll = consumer.poll(Duration.ofSeconds(1, 3000));
            Set<TopicPartition> partitions = poll.partitions();
            for (TopicPartition partition : partitions) {
                List<ConsumerRecord<String, String>> records = poll.records(partition);
                for (ConsumerRecord<String, String> record : records) {
                    logger.info("分区:{}, 偏移量:{}, 消息内容:{}", record.partition(), record.offset(), record.value());
                }
                ConsumerRecord<String, String> consumerRecord = records.get(records.size() - 1);
                long offset = consumerRecord.offset() + 1;
                consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(offset)));
            }
        }
    }

    /**
     * 当同一主题下有新的消费者上线，kafka服务收到上线信息后会对改主题下各个消费者进行重新分区分配
     * 这个时候这个监听就会启动
     * @see {官网关于该监听器的使用案例 http://kafka.apache.org/0100/javadoc/org/apache/kafka/clients/consumer/ConsumerRebalanceListener.html}
     */
    public static class ConsumerRebalanceListenerImpl implements ConsumerRebalanceListener {

        private Consumer consumer;

        /**
         * 初始化方法，传入consumer对象，否则无法调用外部的consumer对象，必须传入
         *
         * @param consumer
         */
        public ConsumerRebalanceListenerImpl(Consumer consumer) {
            this.consumer = consumer;
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

}
