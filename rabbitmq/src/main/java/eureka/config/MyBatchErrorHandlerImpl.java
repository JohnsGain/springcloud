package eureka.config;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.kafka.listener.BatchErrorHandler;

/**
 * @author ""
 * @description 批量监听时候配置的批量异常处理器
 * @date 2018/9/30
 * @since jdk1.8
 */
public class MyBatchErrorHandlerImpl implements BatchErrorHandler {
    @Override
    public void handle(Exception thrownException, ConsumerRecords<?, ?> data) {

    }
}
