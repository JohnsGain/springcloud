package com.john.flink.longridealert.solution;

import com.john.flink.common.dto.TaxiRide;
import com.john.flink.common.source.TaxiRideGenerator;
import lombok.SneakyThrows;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
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
 * @date 2024-03-15 00:30
 * @since jdk17
 */
public class LongRidesSolution {

    private final SourceFunction<TaxiRide> source;

    private final SinkFunction<Long> sink;

    public LongRidesSolution(SourceFunction<TaxiRide> source, SinkFunction<Long> sink) {
        this.source = source;
        this.sink = sink;
    }

    public static void main(String[] args) {
        TaxiRideGenerator generator = new TaxiRideGenerator();
        SinkFunction<Long> sink = new PrintSinkFunction<>();
        LongRidesSolution solution = new LongRidesSolution(generator, sink);
        solution.execute();
    }

    @SneakyThrows
    public JobExecutionResult execute() {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<TaxiRide> addedSource = environment.addSource(source);
        WatermarkStrategy<TaxiRide> watermarkStrategy = WatermarkStrategy.<TaxiRide>forBoundedOutOfOrderness(Duration.ofSeconds(60))
                .withTimestampAssigner(((element, recordTimestamp) -> element.getEventTimeMillis()));
        addedSource.assignTimestampsAndWatermarks(watermarkStrategy)
                .keyBy(TaxiRide::getRideId)
                .process(new AlertFunction())
                .addSink(sink);
        return environment.execute("Long Taxi Rides");
    }

    private static class AlertFunction extends KeyedProcessFunction<Long, TaxiRide, Long> {

        private ValueState<TaxiRide> valueState;

        @Override
        public void onTimer(long timestamp, KeyedProcessFunction<Long, TaxiRide, Long>.OnTimerContext ctx, Collector<Long> out) throws Exception {
            out.collect(valueState.value().rideId);
            valueState.clear();
        }

        @Override
        public void open(Configuration parameters) throws Exception {
            ValueStateDescriptor<TaxiRide> descriptor = new ValueStateDescriptor<>("ride event", TaxiRide.class);
            valueState = getRuntimeContext().getState(descriptor);
        }

        @Override
        public void processElement(TaxiRide value, KeyedProcessFunction<Long, TaxiRide, Long>.Context ctx, Collector<Long> out) throws Exception {
            TaxiRide taxiRide = valueState.value();
            if (taxiRide == null) {
                valueState.update(value);
                if (value.isStart()) {
                    ctx.timerService().registerEventTimeTimer(getTimerTime(value));
                }
            } else {
                if (value.isStart) {
                    if (rideTooLong(value, taxiRide)) {
                        out.collect(value.rideId);
                    }
                } else {
                    ctx.timerService().deleteEventTimeTimer(getTimerTime(taxiRide));
                    if (rideTooLong(taxiRide, value)) {
                        out.collect(value.rideId);
                    }
                }
                valueState.clear();
            }
        }

        private boolean rideTooLong(TaxiRide startEvent, TaxiRide endEvent) {
            return Duration.between(startEvent.eventTime, endEvent.eventTime)
                    .compareTo(Duration.ofHours(2))
                    > 0;
        }

        private long getTimerTime(TaxiRide ride) throws RuntimeException {
            if (ride.isStart) {
                return ride.eventTime.plusSeconds(120 * 60).toEpochMilli();
            } else {
                throw new RuntimeException("Can not get start time from END event.");
            }
        }
    }

}
