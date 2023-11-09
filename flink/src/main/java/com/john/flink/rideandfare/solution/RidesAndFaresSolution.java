package com.john.flink.rideandfare.solution;

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

    public JobExecutionResult execute() throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        return execute(environment);
    }

    public JobExecutionResult execute(StreamExecutionEnvironment env) throws Exception {
        KeyedStream<TaxiRide, Long> rideLongKeyedStream = env.addSource(rideSource)
                .filter(item -> item.isStart)
                .keyBy(TaxiRide::getRideId);

        KeyedStream<TaxiFare, Long> fareLongKeyedStream = env.addSource(fareSource)
                .keyBy(TaxiFare::getRideId);

        rideLongKeyedStream.connect(fareLongKeyedStream)
                .flatMap(new EnrichmentFunction4Solution())
                .uid("enrichment") // uid for this operator's state
                .name("enrichment") // name for this operator in the web UI
                .addSink(sink);

        return env.execute("Join Rides with Fares");
    }

    public static void main(String[] args) throws Exception {
        TaxiRideGenerator rideGenerator = new TaxiRideGenerator();
        TaxiFareGenerator fareGenerator = new TaxiFareGenerator();
        RidesAndFaresSolution solution = new RidesAndFaresSolution(rideGenerator,fareGenerator,
                new PrintSinkFunction<>());
        // Setting up checkpointing so that the state can be explored with the State Processor API.
        // Generally it's better to separate configuration settings from the code,
        // but for this example it's convenient to have it here for running in the IDE.

        solution.execute();
    }

}
