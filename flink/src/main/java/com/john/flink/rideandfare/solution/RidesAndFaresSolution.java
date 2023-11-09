package com.john.flink.rideandfare.solution;

import com.john.flink.common.dto.RideAndFare;
import com.john.flink.common.dto.TaxiFare;
import com.john.flink.common.dto.TaxiRide;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-11-10 02:20
 * @since jdk17
 */
public class RidesAndFaresSolution {


    private final SourceFunction<TaxiRide> rideSource;
    private final SourceFunction<TaxiFare> fareSource;
    private final SinkFunction<RideAndFare> sink;

    /**
     * Creates a job using the sources and sink provided.
     */
    public RidesAndFaresSolution(
            SourceFunction<TaxiRide> rideSource,
            SourceFunction<TaxiFare> fareSource,
            SinkFunction<RideAndFare> sink) {

        this.rideSource = rideSource;
        this.fareSource = fareSource;
        this.sink = sink;
    }

    public JobExecutionResult execute() {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        return execute(environment);
    }

    public JobExecutionResult execute(StreamExecutionEnvironment env) {
        
    }

}
