package com.john.flink.common.test;

import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.accumulators.ListAccumulator;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.util.List;

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
        getRuntimeContext()
                .addAccumulator(name, new ListAccumulator<OUT>());
    }

    @Override
    public void invoke(OUT value, Context context) throws Exception {
        getRuntimeContext()
                .getAccumulator(name)
                .add(value);
    }

    public List<OUT> getResults(JobExecutionResult result) {
        return result.getAccumulatorResult(name);
    }

}
