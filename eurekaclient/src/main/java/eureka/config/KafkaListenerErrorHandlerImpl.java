package eureka.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @author zhangjuwa
 * @description 监听异常处理配置
 * @date 2018/9/27
 * @since jdk1.8
 */
@Component
public class KafkaListenerErrorHandlerImpl implements KafkaListenerErrorHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(KafkaListenerErrorHandlerImpl.class);

    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException exception) throws Exception {
        exception.printStackTrace();
        LOGGER.info("消费者监听执行异常:{},消费的消息是:{},异常内容:{}", message, message.getPayload(), exception.getMessage());
        return null;
    }
}
