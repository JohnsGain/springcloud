package eureka.config;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;

/**
 * @author zhangjuwa
 * @description 确认一条消息是否应该被废弃，在发生分区重分配的时候，可能会发生消息重复消费问题，
 * In certain scenarios, such as rebalancing, a message may be redelivered that has already been
 * processed. The framework cannot know whether such a message has been processed or not, that is
 * an application-level function. This is known as the Idempotent Receiver pattern and Spring Integration
 * provides an implementation thereof.
 * 实现这个接口之后，可以自行判断什么情况这样消息应该废弃或者不废弃
 * @date 2018/9/30
 * @since jdk1.8
 */
public class RecordFilterStrategyImpl implements RecordFilterStrategy {
    /**
     * 返回true表示should be discarded.
     * @param consumerRecord
     * @return
     */
    @Override
    public boolean filter(ConsumerRecord consumerRecord) {
        return true;
    }
}
