package com.john.flink.common.test;

import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-10-23 23:47
 * @since jdk17
 */
public interface ExecutablePipeline <IN,OUT>{

    JobExecutionResult execute(SourceFunction<IN> source, TestSink<OUT> sink)  throws Exception;
}
