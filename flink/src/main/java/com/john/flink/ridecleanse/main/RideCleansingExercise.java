package com.john.flink.ridecleanse.main;

import com.john.flink.common.MissingSolutionException;
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

    public static void main(String[] args) throws Exception {
        RideCleansingExercise job = new RideCleansingExercise(new TaxiRideGenerator(), new PrintSinkFunction<>());
        job.execute();
    }

    public JobExecutionResult execute() throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        environment.addSource(source)
                .filter(new NYCFilter())
                .addSink(sink);
        return environment.execute("Taxi Ride Cleansing");
    }

    public static class NYCFilter implements FilterFunction<TaxiRide> {

        @Override
        public boolean filter(TaxiRide value) throws Exception {
            throw new MissingSolutionException();
        }
    }
}
