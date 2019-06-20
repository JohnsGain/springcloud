package com.demo.flowable.config;

import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.common.engine.impl.event.FlowableEngineEventImpl;
import org.springframework.stereotype.Component;

/**
 * @author zhangjuwa
 * @date 2019/6/16
 * @since jdk1.8
 **/
@Component
@Slf4j
public class FlowableEventListenerImpl implements FlowableEventListener {
    /**
     * FlowableEvent,这个接口没法取到相关参数，必须要强转型为FlowableEngineEventImpl才可以。
     * @param event
     */
    @Override
    public void onEvent(FlowableEvent event) {
        FlowableEngineEventImpl engineEvent = (FlowableEngineEventImpl) event;
        log.info("监听！！！！！！！！！！={}", engineEvent.getType().name());
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }

    @Override
    public boolean isFireOnTransactionLifecycleEvent() {
        return false;
    }

    @Override
    public String getOnTransaction() {
        return null;
    }
}
