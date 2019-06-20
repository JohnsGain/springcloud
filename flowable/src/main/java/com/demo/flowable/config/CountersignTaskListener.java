package com.demo.flowable.config;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * TaskListener  主要是监听usertask的情况
 * @see {https://blog.csdn.net/nixzmdi1/article/details/88530512}
 * @author zhangjuwa
 * @date 2019/6/16
 * @since jdk1.8
 **/
@Component
@Slf4j
public class CountersignTaskListener implements TaskListener, BeanPostProcessor {

    @Autowired
    private FlowableEventListener flowableEventListener;

    @Override
    public void notify(DelegateTask delegateTask) {
//        StandaloneProcessEngineConfiguration
//        ProcessEngineConfigurationImpl
        log.info(JSON.toJSONString(delegateTask));
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//        if (ProcessEngineConfigurationImpl.class.isAssignableFrom(bean.getClass())) {
//            ProcessEngineConfigurationImpl ben = (ProcessEngineConfigurationImpl) bean;
//            ((ProcessEngineConfigurationImpl) bean).setTypedEventListeners()
//        }
        return bean;
    }
}
