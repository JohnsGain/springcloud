package com.john.flink.common.test;

import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-11-10 02:12
 * @since jdk17
 */
public interface ExecutableTwoInputPipeline<IN1, IN2, OUT> {

    JobExecutionResult execute(
            SourceFunction<IN1> source1, SourceFunction<IN2> source2, TestSink<OUT> sink)
            throws Exception;

}
