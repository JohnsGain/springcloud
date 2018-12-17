package eureka.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * @author zhangjuwa
 * @description 生产者
 * @date 2018/9/26
 * @since jdk1.8
 */
@Component
public class KafkaProducer {

    private static Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 分布式计数器目录
     */
    public static final String COUNTER = "/curator/atomic";

    public static final String TOPIC = "test";

    @Autowired
    private CuratorFramework curatorFramework;

    @Autowired
    private ObjectMapper objectMapper;

    @Transactional(rollbackFor = Exception.class, transactionManager = "kafkaTransactionManager")
    public void send(IMessage message) throws Exception {
        KafkaMessage kafkaMessage = (KafkaMessage) message;

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        DistributedAtomicInteger atomicInteger = new DistributedAtomicInteger(curatorFramework, COUNTER, retryPolicy);
        AtomicValue<Integer> value = atomicInteger.increment();
        kafkaMessage.setId(value.postValue());
        String json = objectMapper.writeValueAsString(kafkaMessage);
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(TOPIC, String.valueOf(kafkaMessage.getId()), json);
        if (future.isDone()) {
            logger.info("+++++++++++++++++++++  message = {}", json);
        }
        //可以在这里添加成功回调或者失败回调
        //future.addCallback();
        //如果想要阻塞发生线程直到发送成功
        //SendResult<String, String> stringStringSendResult = future.get();

        String msg = kafkaMessage.getMessage();
        if (msg.equals("fuck")) {
            throw new RuntimeException("不能发送敏感词消息:" + msg);
        }
    }
}
