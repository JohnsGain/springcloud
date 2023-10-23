package com.john.flink.ridecleanse.solution;

import com.john.flink.common.GeoUtils;
import com.john.flink.common.TaxiRide;
import com.john.flink.common.source.TaxiRideGenerator;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-10-24 00:01
 * @since jdk17
 */
public class RideCleansingSolution {

    private final SourceFunction<TaxiRide> source;
    private final SinkFunction<TaxiRide> sink;

    public RideCleansingSolution(SourceFunction<TaxiRide> source, SinkFunction<TaxiRide> sink) {
        this.source = source;
        this.sink = sink;
    }

    public static void main(String[] args) throws Exception {
        RideCleansingSolution cleansingSolution = new RideCleansingSolution(new TaxiRideGenerator(), new PrintSinkFunction<>());
        cleansingSolution.execute();
    }

    public JobExecutionResult execute() throws Exception {
        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        executionEnvironment.addSource(source)
                .filter(new NYCFilter())
                .addSink(sink);
        return executionEnvironment.execute("Taxi Ride Cleansing");
    }


    public static class NYCFilter implements FilterFunction<TaxiRide> {
        @Override
        public boolean filter(TaxiRide value) throws Exception {
            return GeoUtils.isInNYC(value.getStartLon(), value.getStartLat())
                    && GeoUtils.isInNYC(value.getEndLon(), value.getEndLat());
        }
    }
}

