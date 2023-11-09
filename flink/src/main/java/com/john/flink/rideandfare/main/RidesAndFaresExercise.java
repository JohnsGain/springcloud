package com.john.flink.rideandfare.main;

import com.john.flink.common.dto.RideAndFare;
import com.john.flink.common.dto.TaxiFare;
import com.john.flink.common.dto.TaxiRide;
import com.john.flink.common.source.TaxiFareGenerator;
import com.john.flink.common.source.TaxiRideGenerator;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-11-10 01:00
 * @since jdk17
 */
public class RidesAndFaresExercise {

    private SourceFunction<TaxiRide> rideSource;

    private SourceFunction<TaxiFare> fareSource;

    private SinkFunction<RideAndFare> sink;

    public RidesAndFaresExercise(
            SourceFunction<TaxiRide> rideSource,
            SourceFunction<TaxiFare> fareSource,
            SinkFunction<RideAndFare> sink) {

        this.rideSource = rideSource;
        this.fareSource = fareSource;
        this.sink = sink;
    }

    /**
     * Creates and executes the pipeline using the StreamExecutionEnvironment provided.
     *
     * @return {JobExecutionResult}
     * @throws Exception which occurs during job execution.
     */
    public JobExecutionResult execute() throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();

        KeyedStream<TaxiRide, Long> rideIdKey = environment.addSource(rideSource)
                .filter(item -> item.isStart)
                .keyBy(TaxiRide::getRideId);

        KeyedStream<TaxiFare, Long> fareRideIdKey = environment.addSource(fareSource)
                .keyBy(TaxiFare::getRideId);

        rideIdKey.connect(fareRideIdKey)
                .flatMap(new EnrichmentFunction())
                .addSink(sink);

        return environment.execute("Join Rides with Fares");
    }

    public static void main(String[] args) throws Exception {
        TaxiRideGenerator taxiRideGenerator = new TaxiRideGenerator();
        TaxiFareGenerator fareGenerator = new TaxiFareGenerator();
        RidesAndFaresExercise exercise = new RidesAndFaresExercise(taxiRideGenerator, fareGenerator,
                new PrintSinkFunction<>());

        exercise.execute();
    }

}
