package com.john.flink.common.test;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-10-20 01:25
 * @since jdk17
 */
public class TestSink<OUT> extends RichSinkFunction<OUT> {


    private final String name;

    public TestSink(String name) {
        this.name = name;
    }

    public TestSink() {
        this("results");
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
    }

    @Override
    public void invoke(OUT value, Context context) throws Exception {
        super.invoke(value, context);
    }


}
