package eureka.config;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.ProducerListener;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author zhangjuwa
 * @description 自定义生产者监听实现
 * @date 2018/9/28
 * @since jdk1.8
 */
public class CustomerProducerListenerImpl implements ProducerListener<String, String> {

    private final Logger logger = LoggerFactory.getLogger(CustomerProducerListenerImpl.class);

    @Override
    public void onSuccess(String topic, Integer partition, String key, String value, RecordMetadata recordMetadata) {
        logger.info("成功发送消息!主题:{},分区:{},key:{},vlaue:{}, 消息生成时间:{},偏移量:{}", topic, partition, key, value,
                Instant.ofEpochMilli(recordMetadata.timestamp()).atZone(ZoneId.of("Asia/Shanghai"))
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                recordMetadata.offset());
    }

    @Override
    public void onError(String topic, Integer partition, String key, String value, Exception exception) {
        if (logger.isWarnEnabled()) {
            logger.warn("消息发送失败!主题:{},分区:{},key:{},vlaue:{}", topic, partition, key, value);
        }
    }

    /**
     * 只有返回true，发送成功onSuccess才会收到消息
     *
     * @return
     */
    @Override
    public boolean isInterestedInSuccess() {
        return true;
    }
}
