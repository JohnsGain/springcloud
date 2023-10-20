package eureka.config;

import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author ""
 * @description 设置提交偏移量回调，在提交成功或者失败都会收到通知
 * @date 2018/9/28
 * @since jdk1.8
 */
public class MyLoggingCommitCallback implements OffsetCommitCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyLoggingCommitCallback.class);

    @Override
    public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
        if (exception != null) {
            LOGGER.error("Commit failed for " + offsets, exception);
        } else if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Commits for " + offsets + " completed");
        }
    }
}
