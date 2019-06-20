package com.demo.flowable.config;

import com.google.common.collect.Lists;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 1.配置 CustomEngineConfigurationConfigurerImpl 为防止生成的流程图中中文乱码
 * 2.flowable实现流程全局事件配置
 *
 * @author zhangjuwa
 * @date 2019/6/19
 * @since jdk1.8
 **/
@Configuration
public class CustomEngineConfigurationConfigurerImpl implements EngineConfigurationConfigurer<SpringProcessEngineConfiguration> {

    private final FlowableEventListener flowableEventListener;

    @Autowired
    public CustomEngineConfigurationConfigurerImpl(FlowableEventListener flowableEventListener) {
        this.flowableEventListener = flowableEventListener;
    }

    /**
     * 所有的事件类型定义在{@link FlowableEngineEventType}
     *
     * @param springProcessEngineConfiguration
     */
    @Override
    public void configure(SpringProcessEngineConfiguration springProcessEngineConfiguration) {
        springProcessEngineConfiguration.setActivityFontName("宋体");
        springProcessEngineConfiguration.setLabelFontName("宋体");
        springProcessEngineConfiguration.setAnnotationFontName("宋体");
        Map<String, List<FlowableEventListener>> typedEventListeners = new HashMap<>();
        typedEventListeners.put(FlowableEngineEventType.TASK_CREATED.name(), Lists.newArrayList(flowableEventListener));
        springProcessEngineConfiguration.setTypedEventListeners(typedEventListeners);
    }

}
