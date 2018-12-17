package eureka.config;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListenerAnnotationBeanPostProcessor;
import org.springframework.kafka.annotation.KafkaListeners;
import org.springframework.kafka.config.*;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.BatchAcknowledgingMessageListener;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.adapter.BatchMessagingMessageListenerAdapter;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * spring已经配置了下面所有bean,这里是手动配置，自定义生产者监听工厂
 * 启用注解{@link EnableKafka}，再加上配置一个kafkaListenerContainerFactory，
 * 就可以支持在处理消息的方法上直接获取POJO对象来消费，不用{@link org.apache.kafka.clients.consumer.ConsumerRecord}
 *
 * @author zhangjuwa
 * @description kafka配置
 * @date 2018/9/28
 * @see {@link org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration}
 * @since jdk1.8
 */
@Configuration
@EnableConfigurationProperties({KafkaProperties.class})
@EnableKafka
public class KafkaConfig {

    private final Logger logger = LoggerFactory.getLogger(KafkaConfig.class);

    @Autowired
    private KafkaProperties kafkaProperties;

    @Autowired
    private KafkaConsumerClient kafkaConsumerClient;

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public RedisTemplate<String, Serializable> redisTemplate() {
        RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory(redisClusterConfiguration());
        connectionFactory.afterPropertiesSet();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.afterPropertiesSet();
        //启用事务支持
        redisTemplate.setEnableTransactionSupport(false);
        return redisTemplate;
    }

    /**
     * 要显示配置redis集群配置bean，否则不走集群配置获取连接，直接走单机连接池
     *
     * @return
     */
    @Bean
    public RedisClusterConfiguration redisClusterConfiguration() {
        return new RedisClusterConfiguration(Lists.newArrayList("DDBtest008:6379"));
    }

    /**
     * 配置kafakAdmin,可以在客户端添加主题
     *
     * @return
     */
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        KafkaAdmin kafkaAdmin = new KafkaAdmin(configs);
        //默认某broker不可用，上下文继续加载,只是对不可用broker做日志记录
        //如果设置为true,当配置的kafka服务集群中某节点不可用时，上下文会结束加载，
        kafkaAdmin.setFatalIfBrokerNotAvailable(true);
        return kafkaAdmin;
    }

    /**
     * 配置一个新主题
     *
     * @return
     */
    @Bean
    public NewTopic test2() {
        NewTopic topic = new NewTopic("test2", 3, (short) 1);
        return topic;
    }

    /**
     * 如果要是{@link KafkaTransactionManager} 生效，生产者工厂要启用事务，即设置事务id前缀
     * {@link KafkaTransactionManager}
     *
     * @return
     */
    @Bean
    public ProducerFactory<String, String> producerFactory() {
        DefaultKafkaProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory<>(kafkaProperties.buildProducerProperties());
        //当配置一个事务前缀的时候，就启动了事务，事务管理会负责管理多个启用了事务的生产者，
        //每个生产者的事务ID是事务前缀 + n,n是随着生产者数量递增，从0开始
        producerFactory.setTransactionIdPrefix(applicationName + "_kafka_trans_");
        logger.info("是否支持事务:{}", producerFactory.transactionCapable());

        return producerFactory;
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(producerFactory());
        kafkaTemplate.setProducerListener(kafkaProducerListener());
        kafkaTemplate.setDefaultTopic(kafkaProperties.getTemplate().getDefaultTopic());
        return kafkaTemplate;
    }

    /**
     * 配置生产者监听，在发送成功或者失败都会收到监听信息
     *
     * @return
     */
    @Bean
    public ProducerListener<String, String> kafkaProducerListener() {
        return new CustomerProducerListenerImpl();
    }

    /**
     * 配置kafka事务管理，需要传入一个支持事务的生产者工厂
     *
     * @return
     */
    @Bean
    public KafkaTransactionManager<String, String> kafkaTransactionManager() {
        KafkaTransactionManager<String, String> kafkaTransactionManager = new KafkaTransactionManager<>(producerFactory());
        kafkaTransactionManager.setNestedTransactionAllowed(true);
        //kafkaTransactionManager.setFailEarlyOnGlobalRollbackOnly();
        //kafkaTransactionManager.setRollbackOnCommitFailure();
        kafkaTransactionManager.setValidateExistingTransaction(true);
        return kafkaTransactionManager;
    }

    /**
     * 目前我的理解：同一个主题的消费者可以使用同一个消费者工厂，这涉及到消费者工厂配置里面设置的并发数量，
     * 他会为每一个@KafkaListener配置的方法生成并发数的个数的消费者实例来并发执行，他为每一个消费者实例生成应用数据信息的时候
     * 会去clientId作为前缀，所以不同主题下的消费者工厂配置的clientId不应该相同，否则会报错
     *
     * @return
     */
    @Bean
    public ConsumerFactory<?, ?> kafkaConsumerFactory() {
        Map<String, Object> stringObjectMap = this.kafkaProperties.buildConsumerProperties();
        stringObjectMap.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, "org.apache.kafka.clients.consumer.RoundRobinAssignor");
        stringObjectMap.put(ConsumerConfig.EXCLUDE_INTERNAL_TOPICS_CONFIG, true);
        //目前设置为同一个主题下就是同一个客户端
        stringObjectMap.put(ConsumerConfig.CLIENT_ID_CONFIG, kafkaProperties.getTemplate().getDefaultTopic());
        return new DefaultKafkaConsumerFactory<>(stringObjectMap);
    }

    /**
     * 第二种配置kafka事务的方式：在配置的消息监听容器里面配置事务管理实例
     * 第一种配置kafka事务的方式，在生产者工厂实例配置一个事务ID前缀，然后在需要kafka事务的地方
     * 启用spring自带的事务注解{@link org.springframework.transaction.annotation.Transactional}即可支持
     *
     * @param kafkaTemplate
     * @return
     */
    //@Bean
    public KafkaMessageListenerContainer kafkaMessageListenerContainer(KafkaTemplate kafkaTemplate) {
        //ConcurrentMessageListenerContainer
        //KafkaListenerEndpointRegistrar
        //RecordMessagingMessageListenerAdapter
        ContainerProperties properties = new ContainerProperties(kafkaProperties.getTemplate().getDefaultTopic());
        properties.setGroupId(kafkaProperties.getConsumer().getGroupId());
        properties.setCommitCallback(new MyLoggingCommitCallback());
        //设置在listener 发生异常的时候是否提交偏移量
        //properties.setAckOnError();
        //回调模式，成功处理一批消息之后回调，或者成功处理单条消息之后回调，或者在确定的时间之后回调等

        properties.setMessageListener((MessageListener<String, String>) m -> {
            kafkaTemplate.sendDefault("bar");
            kafkaTemplate.sendDefault("qux");
            kafkaTemplate.sendOffsetsToTransaction(Collections.singletonMap(new TopicPartition(m.topic(), m.partition()),
                    new OffsetAndMetadata(m.offset() + 1)));
        });
        //配置kafka事务，我这里使用了生产者实例里面配置事务，所以这里不配置了
        //properties.setTransactionManager(kafkaTransactionManager());

        ConsumerFactory<?, ?> consumerFactory = kafkaConsumerFactory();
        KafkaMessageListenerContainer listenerContainer = new KafkaMessageListenerContainer(consumerFactory, properties);
        return listenerContainer;
    }

    /**
     * 源码阅读解析：{@link KafkaListenerAnnotationBeanPostProcessor#postProcessAfterInitialization}这个类里面这个方法，
     * 会在这两个带有@KafkaListener或者{@link KafkaListeners}的Bean被注入容器后被查找出然后实例化{@link KafkaListenerEndpoint}
     * 会生成{@link MethodKafkaListenerEndpoint}对象和容器工厂，前提是KafkaListeners注解里面配置的容器工厂beanId,否则这里容器工厂为Null,
     * 然后用这类里面的{@link KafkaListenerEndpointRegistrar#registerEndpoint(KafkaListenerEndpoint, KafkaListenerContainerFactory)}
     * 把每一个endpoint和工厂保装成{@link KafkaListenerEndpointRegistrar.KafkaListenerEndpointDescriptor}让入集合，
     * 然后在{@link KafkaListenerEndpointRegistrar}注入bean容器之后，执行{{@link KafkaListenerEndpointRegistrar#afterPropertiesSet()}
     * 里面方法遍历KafkaListenerEndpointDescriptor集合元素，针对每一个元素，先检查容器工厂是否为null,为Null要通过内部注入的工厂beanName
     * 这个容器工厂beanName默认为{@link KafkaListenerAnnotationBeanPostProcessor#DEFAULT_KAFKA_LISTENER_CONTAINER_FACTORY_BEAN_NAME}
     * 从beanFactory里面获取这个beanName对于的容器工厂实例(这里就是我们可以配置的点，自己配置kafkaListenerContainerFactory这beanId的容器工厂进去)，
     * 然后进行下一步:调用
     * {@link KafkaListenerEndpointRegistry#registerListenerContainer(KafkaListenerEndpoint, KafkaListenerContainerFactory)}
     * 里面会把创建的监听容器放入一个集合，然后维护一个List类型的bean，集合元素是创建的监听容器，这集合的beanId是endpoint传入的groupId,
     * 也就是KafkaListener配置的groupId属性
     * <p>
     * {@link ConcurrentMessageListenerContainer#doStart()}里面会根据设置的concurrency，遍历生成多个消息监听容器实例，然后启动，让入
     * 一个集合，
     *
     * @return
     */
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> batchContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> containerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        containerFactory.setConcurrency(1);
        /*
        设置为批量监听，可以在注解了@KafkaListener的方法里面传递参数集合，批量处理,
        还需要在消息监听器里面设置BatchMessagingMessageListenerAdapter，
        如果不是批量监听，则需要消息监听器里面设置RecordMessagingMessageListenerAdapter
        */
        containerFactory.setBatchListener(true);
        containerFactory.setConsumerFactory(getBatchConsumerFactoryConfig());
        ContainerProperties containerProperties = containerFactory.getContainerProperties();
        containerProperties.setPollTimeout(kafkaProperties.getListener().getPollTimeout());
        //默认就是同步提交，目前异步提交不太可靠https://www.confluent.io/blog/transactions-apache-kafka/
        containerProperties.setSyncCommits(true);
        //The interval between checks for a non-responsive consumer in seconds,默认30s
        containerProperties.setGroupId(kafkaProperties.getConsumer().getGroupId());
        containerProperties.setMonitorInterval(10);
        containerProperties.setAckMode(kafkaProperties.getListener().getAckMode());
        containerProperties.setNoPollThreshold(1);
       /* You can configure the listener container to publish a ListenerContainerIdleEvent when some
        time passes with no message delivery. While the container is idle, an event will be published every
        idleEventInterval milliseconds.*/
        containerProperties.setIdleEventInterval(30000L);
        //因为设置了批量监听，所以这里需要批量消息监听器，kafka提交了批量监听适配器
        try {
            BatchAcknowledgingMessageListener<String, String> batch = new BatchMessagingMessageListenerAdapter<>(kafkaConsumerClient,
                    KafkaConsumerClient.class.getMethod("batch", List.class));
            containerProperties.setMessageListener(batch);
            containerFactory.setRecordFilterStrategy(new RecordFilterStrategyImpl());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
      /*  These can be configured with a RetryTemplate and RecoveryCallback<Void> - see the springretry
        project for information about these components. If a recovery callback is not provided, the exception
        is thrown to the container after retries are exhausted. In that case, the ErrorHandler will be invoked,
        if configured, or logged otherwise.
*/
        //containerFactory.setRetryTemplate();
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(20);
        taskScheduler.setThreadFactory(new BasicThreadFactory.Builder().daemon(false).namingPattern("kafkatask-pool-%d").build());
        taskScheduler.initialize();
        containerProperties.setScheduler(taskScheduler);
        //containerProperties.setConsumerTaskExecutor();
        //ConcurrentKafkaListenerContainerFactoryConfigurerd
        containerFactory.setAutoStartup(true);

        return containerFactory;
    }

    ///**
    // * Create a {@link HandlerAdapter} for this listener adapter.
    // * @param messageListener the listener adapter.
    // * @return the handler adapter.
    // */
    //protected HandlerAdapter configureListenerAdapter(MessagingMessageListenerAdapter<String, String> messageListener) {
    //    InvocableHandlerMethod invocableHandlerMethod =
    //            this.messageHandlerMethodFactory.createInvocableHandlerMethod(getBean(), getMethod());
    //    return new HandlerAdapter(invocableHandlerMethod);
    //}
    //

    private ConsumerFactory<String, String> getBatchConsumerFactoryConfig() {
        Map<String, Object> stringObjectMap = this.kafkaProperties.buildConsumerProperties();
        stringObjectMap.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, "org.apache.kafka.clients.consumer.RoundRobinAssignor");
        stringObjectMap.put(ConsumerConfig.EXCLUDE_INTERNAL_TOPICS_CONFIG, true);
        //目前设置为同一个主题下就是同一个客户端
        stringObjectMap.put(ConsumerConfig.CLIENT_ID_CONFIG, "test2");
        return new DefaultKafkaConsumerFactory<>(stringObjectMap);
    }
}
