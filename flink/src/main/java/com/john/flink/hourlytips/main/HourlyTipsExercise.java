package com.john.flink.hourlytips.main;

import com.john.flink.common.dto.TaxiFare;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-12-12 00:56
 * @since jdk17
 */
public class HourlyTipsExercise {

    private final SourceFunction<TaxiFare> source;

    private final SinkFunction<Tuple3<Long, Long, Double>> sink;

    /**
     * Creates a job using the source and sink provided.
     */
    public HourlyTipsExercise(
            SourceFunction<TaxiFare> source, SinkFunction<Tuple3<Long, Long, Double>> sink) {

        this.source = source;
        this.sink = sink;
    }

    public static void main(String[] args) {

    }
}
