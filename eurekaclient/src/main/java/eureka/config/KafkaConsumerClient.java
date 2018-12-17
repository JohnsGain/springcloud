package eureka.config;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.record.TimestampType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.KafkaListenerAnnotationBeanPostProcessor;
import org.springframework.kafka.annotation.KafkaListeners;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;
import org.springframework.kafka.listener.AfterRollbackProcessor;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @Author zhangjuwa
 * @Description kafka消费配置
 * 0.11.0.0版本新功能
 * 0.支持事务{@link KafkaTransactionManager}
 * 1.支持消费者监听消费的时候产生的异常处理，通过{@link KafkaListener}配置
 * 2.支持管理客户端创建主题 {@link org.apache.kafka.clients.admin.AdminClient}
 * 3.MessageHeaders,可以把messageHeaders映射到spring-message的{@link MessageHeaders}
 * 4.添加带有时间戳的消息
 * 5.添加回滚处理逻辑{@link AfterRollbackProcessor}
 * <p>
 * 关于kafkad exactly once {https://www.confluent.io/blog/exactly-once-semantics-are-possible-heres-how-apache-kafka-does-it/}
 * 关于事务 {https://www.confluent.io/blog/transactions-apache-kafka/}
 * @date 2018/9/26
 * @since jdk1.8
 */
@Component
@EnableAutoConfiguration
public class KafkaConsumerClient {

    public static Logger logger;

    static {
        logger = LoggerFactory.getLogger(KafkaConsumerClient.class);
    }

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ConsumerFactory consumerFactory;

    @Autowired
    private KafkaProperties kafkaProperties;

    @Autowired
    private KafkaListenerContainerFactory kafkaListenerContainerFactory;

    //@Autowired
    //public KafkaConsumer(KafkaTemplate<String, String> kafkaTemplate, ConsumerFactory consumerFactory,
    //                     KafkaProperties kafkaProperties) {
    //    this.kafkaTemplate = kafkaTemplate;
    //    this.consumerFactory = consumerFactory;
    //    this.kafkaProperties = kafkaProperties;
    //}

    //@Autowired
    //private KafkaListenerEndpointRegistrar kafkaListenerEndpointRegistrar;

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    //@Autowired
    private KafkaListenerEndpointRegistrar kafkaListenerEndpointRegistrar;

    //@Autowired
    //private ConcurrentMessageListenerContainer concurrentMessageListenerContainer;


    /**
     * @param consumerRecord
     * @see {@link KafkaListenerAnnotationBeanPostProcessor}在这个类里面，过滤出所有带有Kafkalistener注解的类或者方法
     */
    //@KafkaListeners(value = {@KafkaListener(id = "test", topics = {"test"}, errorHandler = "kafkaListenerErrorHandlerImpl")})
    @KafkaListeners(value = {@KafkaListener(group = "test", id = "test", topics = {"test"}, containerFactory = "kafkaListenerContainerFactory")})
    public void consume(ConsumerRecord<String, String> consumerRecord) {
        //KafkaListenerEndpoint endpoint = new MultiMethodKafkaListenerEndpoint<>()
        //kafkaListenerEndpointRegistry.
        logger.info("消费的线程是:{}", Thread.currentThread().getName());

        TimestampType timestampType = consumerRecord.timestampType();
        logger.info("消息生成的时间戳类型:{}", timestampType.name);
        //kafkaTemplate.execute()
        //KafkaMessageListenerContainer
        //MessageHeaders
        logger.info(consumerFactory.getConfigurationProperties().toString());
        //ConsumerConfig
        Optional<String> optional = Optional.ofNullable(consumerRecord.value());
        if (optional.isPresent()) {
            String value = optional.get();
            logger.info("{} - {} - {} : {}", consumerRecord.topic(), consumerRecord.partition(), consumerRecord.key(), value);
        }

        //可以抓取消息头里面的数据，每条消息都有的属性
        Headers headers = consumerRecord.headers();
        for (Header header : headers) {
            logger.info("正在消费的消息{} - {}", header.key(), header.value());
        }

    }


    ///**
    // * 也支持这种直接拿POJO类数据来开始消费，需要在kafka配置类上面启用{@link org.springframework.kafka.annotation.EnableKafka}
    // * 并且配置监听容器{@link ConcurrentMessageListenerContainer}
    // *
    // * @param consumerRecord
    // */
    //@KafkaListener(id = "test2", topics = {"test2"}, containerFactory = "kafkaListenerContainerFactory")
    //public void consumePOJO(ConsumerRecord<String, String> consumerRecord) {
    //
    //}

    ///**
    // * Each partition can be specified in the partitions or partitionOffsets attribute, but not both
    // *
    // * @param record
    // */
    //@KafkaListener(id = "bar", topicPartitions =
    //        {@TopicPartition(topic = "topic1", partitions = {"0", "1"}),
    //                @TopicPartition(topic = "topic2", partitions = "0",
    //                        partitionOffsets = @PartitionOffset(partition = "1", initialOffset = "100"))
    //        })
    //public void listen(ConsumerRecord<?, ?> record) {
    //
    //}

    ///**
    // * e, the listener can also be provided with the Acknowledgment; this
    // * example also shows how to use a different container factory.
    // * @param data
    // * @param ack
    // */
    //@KafkaListener(id = "baz", topics = "myTopic",
    //        containerFactory = "kafkaManualAckListenerContainerFactory")
    //public void listen(String data, Acknowledgment ack) {
    //    ack.acknowledge();
    //}

    //@KafkaListener(id = "qux", topicPattern = "myTopic1")
    //public void listen(@Payload String foo,
    //                   @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) Integer key,
    //                   @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
    //                   @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
    //                   @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts
    //) {
    //}

    /**
     * 可以配置批量监听，需要配置监听容器工厂，设置批量监听属性为true
     *
     * @param list
     */
    @KafkaListener(group = "test2", id = "test2", topics = "test2", containerFactory = "batchContainerFactory")
    public void batch(List<ConsumerRecord> list) {
    }


    /**
     * 当使用{@link AbstractMessageListenerContainer.AckMode}中的MANUAL,MANUAL_IMMEDIATE回调模式
     * 需要传递第二个参数，
     * @param list
     * @param ack
     */
  /*  @KafkaListener(id = "listCRsAck", topics = "myTopic", containerFactory = "batchFactory")
    public void listen(List<ConsumerRecord<Integer, String>> list, Acknowledgment ack) {
    }*/

}
