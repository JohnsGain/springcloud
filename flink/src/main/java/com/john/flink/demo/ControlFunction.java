package com.john.flink.demo;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.co.RichCoFlatMapFunction;
import org.apache.flink.util.Collector;

/**
 * 两个 source 数据合并处理逻辑
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2023-11-09 00:55
 * @since jdk17
 */
public class ControlFunction extends RichCoFlatMapFunction<String, String, String> {

    private ValueState<Boolean> blocked;

    @Override
    public void open(Configuration parameters) throws Exception {
        ValueStateDescriptor<Boolean> stateDescriptor = new ValueStateDescriptor<>("blocked", Boolean.class);
        blocked = super.getRuntimeContext()
                .getState(stateDescriptor);
    }

    @Override
    public void flatMap1(String value, Collector<String> out) throws Exception {
        blocked.update(Boolean.TRUE);
    }

    @Override
    public void flatMap2(String value, Collector<String> out) throws Exception {
        if (blocked.value() == null) {
            out.collect(value);
        }
    }
}
