package eureka.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eureka.common.Result;
import eureka.config.IMessage;
import eureka.config.KafkaMessage;
import eureka.config.KafkaProducer;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.metadata.ItemMetadata;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author ""
 * @description 制作一个kafka生产者消费者客户端，部署在同一个服务器上
 * @date 2018/9/25
 * @since jdk1.8
 */
@RestController
@RequestMapping("kafka")
public class KafkaController {

    @Autowired
    private KafkaAdmin kafkaAdmin;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 获取kafkaadmin配置消息
     */
    @GetMapping("admin")
    public Object produce() {
        return kafkaAdmin.getConfig();
    }

    @GetMapping("message/{value}")
    public Result<Boolean> send(@PathVariable("value") String value) {
        IMessage message = new KafkaMessage();
        message.setMessage(value);
        try {
            kafkaProducer.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.<Boolean>build().error(HttpStatus.INTERNAL_SERVER_ERROR).withData(false).withMessage(e.getMessage());
        }
        return Result.<Boolean>build().ok().withData(true);
    }

    @Autowired
    private ApplicationContext applicationContext;

    @GetMapping("/{group}")
    public Object group(@PathVariable("group") String group) throws JsonProcessingException {
        List<MessageListenerContainer> bean = applicationContext.getBean(group, List.class);
        for (MessageListenerContainer container : bean) {
            Map<String, Map<MetricName, ? extends Metric>> metrics = container.metrics();
        }
        return objectMapper.writeValueAsString(bean);
    }
}
