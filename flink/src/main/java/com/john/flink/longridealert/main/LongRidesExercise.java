package com.john.flink.longridealert.main;

import com.john.flink.common.MissingSolutionException;
import com.john.flink.common.dto.TaxiRide;
import com.john.flink.common.source.TaxiRideGenerator;
import lombok.SneakyThrows;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.util.Collector;

import java.time.Duration;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2024-03-15 00:18
 * @since jdk17
 */
public class LongRidesExercise {

    private final SourceFunction<TaxiRide> source;

    private final SinkFunction<Long> sink;

    public LongRidesExercise(SourceFunction<TaxiRide> source, SinkFunction<Long> sink) {
        this.source = source;
        this.sink = sink;
    }

    @SneakyThrows
    public static void main(String[] args) {
        TaxiRideGenerator generator = new TaxiRideGenerator();
        SinkFunction<Long> sin = new PrintSinkFunction<>();
        LongRidesExercise exercise = new LongRidesExercise(generator, sin);
        exercise.execute();
    }

    public JobExecutionResult execute() throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<TaxiRide> addedSource = environment.addSource(source);
        WatermarkStrategy<TaxiRide> watermarkStrategy = WatermarkStrategy.<TaxiRide>forBoundedOutOfOrderness(Duration.ofSeconds(60))
                .withTimestampAssigner(((element, recordTimestamp) -> element.getEventTimeMillis()));
        addedSource.assignTimestampsAndWatermarks(watermarkStrategy)
                .keyBy(item -> item.getRideId())
                .process(new AlertFunction())
                .addSink(sink);

        return environment.execute("Long Taxi Rides");
    }

    private static class AlertFunction extends KeyedProcessFunction<Long, TaxiRide, Long> {

        @Override
        public void onTimer(long timestamp, KeyedProcessFunction<Long, TaxiRide, Long>.OnTimerContext ctx, Collector<Long> out) throws Exception {
            throw new MissingSolutionException();
        }

        @Override
        public void open(Configuration parameters) throws Exception {
            throw new MissingSolutionException();
        }

        @Override
        public void processElement(TaxiRide value, KeyedProcessFunction<Long, TaxiRide, Long>.Context ctx, Collector<Long> out) throws Exception {
            throw new MissingSolutionException();
        }
    }

}
