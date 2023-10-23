package com.john.flink.common.test;

import com.john.flink.common.MissingSolutionException;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-10-23 23:46
 * @since jdk17
 */
public class ComposedPipeline<IN, OUT> implements ExecutablePipeline<IN, OUT> {
    private static Logger log = LogManager.getLogger(ComposedPipeline.class);
    private final ExecutablePipeline<IN, OUT> exercise;
    private final ExecutablePipeline<IN, OUT> solution;

    public ComposedPipeline(ExecutablePipeline<IN, OUT> exercise, ExecutablePipeline<IN, OUT> solution) {
        this.exercise = exercise;
        this.solution = solution;
    }


    @Override
    public JobExecutionResult execute(SourceFunction<IN> source, TestSink<OUT> sink) throws Exception {
        JobExecutionResult result = null;
        try {
            result = exercise.execute(source, sink);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            if (MissingSolutionException.ultimateCauseIsMissingSolution(e)) {
                log.warn("缺少解决方案异常");
                result = solution.execute(source, sink);
            } else {
                throw e;
            }
        }
        return result;
    }
}
