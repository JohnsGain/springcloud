package com.shulian.neo4j.config;

import org.neo4j.ogm.session.event.Event;
import org.neo4j.ogm.session.event.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author zhangjuwa
 * @description 实现neo4j事件监听逻辑
 * @date 2018/9/5
 * @since jdk1.8
 */
@Component
public class Neo4jEventListenerImpl implements EventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(Neo4jEventListenerImpl.class);

    @Override
    public void onPreSave(Event event) {
        LOGGER.info("正在添加节点" + event.getObject());
    }

    @Override
    public void onPostSave(Event event) {

    }

    @Override
    public void onPreDelete(Event event) {

    }

    @Override
    public void onPostDelete(Event event) {

    }
}
