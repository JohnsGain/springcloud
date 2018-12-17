package eureka.config;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * @author zhangjuwa
 * @description 类级别的消息监听
 * @date 2018/9/29
 * @since jdk1.8
 */
public class ClassLevelKafkaListener {
  /*  @KafkaListener(id = "multi", topics = "myTopic")
    static class MultiListenerBean {
        @KafkaHandler
        public void listen(String foo) {
 ...
        }
        @KafkaHandler
        public void listen(Integer bar) {
 ...
        }
    }*/

}
