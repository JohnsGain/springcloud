package com.demo.flowable.config;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

/**
 * ExecutionListener 主要是监听 sequenceFlow的监听器，
 *
 * @author zhangjuwa
 * @date 2019/6/18
 * @see {https://blog.csdn.net/nixzmdi1/article/details/88530512}
 * @since jdk1.8
 **/
@Component
@Slf4j
public class SequenceFlowListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution execution) {
        log.info("被启用，监听到={}", execution.getEventName());
        execution.setVariable("createTime", System.currentTimeMillis());
    }
}
