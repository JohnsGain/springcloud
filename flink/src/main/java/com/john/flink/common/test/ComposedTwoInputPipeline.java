package com.john.flink.common.test;

import com.john.flink.common.MissingSolutionException;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-11-10 02:13
 * @since jdk17
 */
public class ComposedTwoInputPipeline<IN1, IN2, OUT> implements ExecutableTwoInputPipeline<IN1, IN2, OUT> {

    private final ExecutableTwoInputPipeline<IN1, IN2, OUT> exercise;
    private final ExecutableTwoInputPipeline<IN1, IN2, OUT> solution;

    public ComposedTwoInputPipeline(
            ExecutableTwoInputPipeline<IN1, IN2, OUT> exercise,
            ExecutableTwoInputPipeline<IN1, IN2, OUT> solution) {

        this.exercise = exercise;
        this.solution = solution;
    }

    @Override
    public JobExecutionResult execute(SourceFunction<IN1> source1, SourceFunction<IN2> source2, TestSink<OUT> sink)
            throws Exception {
        JobExecutionResult jobExecutionResult;
        try {
            jobExecutionResult = exercise.execute(source1, source2, sink);
        } catch (Exception e) {
            if (MissingSolutionException.ultimateCauseIsMissingSolution(e)) {
                jobExecutionResult = solution.execute(source1, source2, sink);
            } else {
                throw e;
            }
        }
        return jobExecutionResult;
    }
}
