package com.john.flink.ridecleanse.main;

import com.john.flink.common.TaxiRide;
import com.john.flink.common.source.TaxiRideGenerator;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-10-19 00:40
 * @since jdk17
 */
public class RideCleansingExercise {

    private final SourceFunction<TaxiRide> source;

    private final SinkFunction<TaxiRide> sink;

    /**
     * Creates a job using the source and sink provided.
     */
    public RideCleansingExercise(SourceFunction<TaxiRide> source, SinkFunction<TaxiRide> sink) {

        this.source = source;
        this.sink = sink;
    }

    public static void main(String[] args) {
        RideCleansingExercise exercise = new RideCleansingExercise(new TaxiRideGenerator(), null);
    }

}
