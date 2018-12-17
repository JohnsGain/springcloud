package com.shulian.bus.amqp;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangjuwa
 * @description 队列配置
 * @date 2018/4/23
 * @since jdk1.8
 */
@Configuration
public class QueueConfig {

    @Bean
    public Queue queue() {
        return new Queue("hello");
    }
    @Bean
    public Queue userQueue() {
        return new Queue("user");
    }

    //===============以下是验证topic Exchange的队列==========

    @Bean
    public Queue queueMessage() {
        return  new Queue("topic.message");
    }

    @Bean
    public Queue queueMessages() {
        return new Queue("topic.messages");
    }

    //===============以下是验证Fanout Exchange的队列==========
    @Bean
    public Queue aMessage() {
        return new Queue("fanout.A");
    }

    @Bean
    public Queue bMessage() {
        return new Queue("fanout.B");
    }

    @Bean
    public Queue cMessage() {
        return new Queue("fanout.C");
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("topicExchange");
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanoutExchange");
    }


    /**
     * 将队列topic.message与exchange绑定，binding_key为topic.message,就是完全匹配
     * @param queueMessage
     * @param topicExchange
     * @return
     */
    @Bean
    public Binding bindMessage(Queue queueMessage, TopicExchange topicExchange) {
        return BindingBuilder.bind(queueMessage).to(topicExchange).with("topic.message");
    }

    /**
     * 将队列topic.messages与exchange绑定，binding_key为topic.#,模糊匹配
     * @param queueMessages
     * @param topicExchange
     * @return
     */
    @Bean
    public Binding bindMessages(Queue queueMessages, TopicExchange topicExchange) {
        return BindingBuilder.bind(queueMessages).to(topicExchange).with("topic.#");
    }

    @Bean
    public Binding bindingA(Queue aMessage, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(aMessage).to(fanoutExchange);
    }

    @Bean
    public Binding bindingB(Queue bMessage, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(bMessage).to(fanoutExchange);
    }

    @Bean
        public Binding bindingC(Queue cMessage, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(cMessage).to(fanoutExchange);
    }

}
