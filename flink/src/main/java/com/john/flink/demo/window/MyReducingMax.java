package com.john.flink.demo.window;

import org.apache.flink.api.common.functions.ReduceFunction;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-12-11 23:53
 * @since jdk17
 */
public class MyReducingMax implements ReduceFunction<SensorReading> {
    @Override
    public SensorReading reduce(SensorReading s1, SensorReading s2) throws Exception {
        return s1.getValue() > s2.getValue() ? s1 : s2;
    }
}
