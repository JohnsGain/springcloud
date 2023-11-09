package com.john.flink.demo;


import com.john.flink.common.GeoUtils;
import com.john.flink.common.dto.TaxiRide;
import com.john.flink.common.source.TaxiRideGenerator;
import com.john.flink.ridecleanse.solution.RideCleansingSolution;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;
import org.joda.time.Duration;
import org.joda.time.Minutes;
import org.junit.Test;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-11-02 01:12
 * @since jdk17
 */
public class StatelessTransformationDemo {
    /**
     * This section covers map() and flatmap(), the basic operations used to implement stateless transformations.
     * The examples in this section assume you are familiar with the Taxi Ride data used in the hands-on exercises
     * in the flink-training-repo .
     */
    @Test
    public void map() throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        TaxiRideGenerator taxiRideGenerator = new TaxiRideGenerator();
        DataStreamSource<TaxiRide> rides = environment.addSource(taxiRideGenerator);
        SingleOutputStreamOperator<EnrichedRideDemo> enrichedNYCRides = rides.filter(new RideCleansingSolution.NYCFilter())
                .map(new Enrichment());

        enrichedNYCRides.print();

        System.out.println("开始执行");
        environment.execute();
    }

    @Test
    public void flatmap() throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<TaxiRide> addedSource = environment.addSource(new TaxiRideGenerator());
        SingleOutputStreamOperator<EnrichedRideDemo> flatted = addedSource.flatMap(new NYCEnrichment());
        flatted.print();

        environment.execute();
    }

    @Test
    public void keyBy1() throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<TaxiRide> addSource = environment.addSource(new TaxiRideGenerator());
        SingleOutputStreamOperator<EnrichedRideDemo> flattedMap = addSource.flatMap(new NYCEnrichment());

        KeyedStream<EnrichedRideDemo, Integer> keyedBy = flattedMap.keyBy((KeySelector<EnrichedRideDemo, Integer>) EnrichedRideDemo::getStartCell);

    }

    @Test
    public void keyBy2() throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<TaxiRide> source = environment.addSource(new TaxiRideGenerator());
        SingleOutputStreamOperator<EnrichedRideDemo> flatMap = source.flatMap(new NYCEnrichment());
        KeyedStream<EnrichedRideDemo, Integer> keyed = flatMap.keyBy(item -> GeoUtils.mapToGridCell(item.getStartLon(), item.getStartLat()));

    }

    @Test
    public void aggregation() throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<TaxiRide> addedSource = environment.addSource(new TaxiRideGenerator());
        SingleOutputStreamOperator<EnrichedRideDemo> streamOperator = addedSource.map(new Enrichment());

        SingleOutputStreamOperator<Tuple2<Integer, Minutes>> flattedMap = streamOperator.flatMap((FlatMapFunction<EnrichedRideDemo, Tuple2<Integer, Minutes>>) (value, out) -> {
            if (!value.isStart) {
                out.collect(new Tuple2<>(value.startCell, Duration.millis(100L).toStandardMinutes()));
            }
        });

        flattedMap.keyBy(item -> item.f0)
                .maxBy(1)
                .print();

    }




    public static class NYCEnrichment implements FlatMapFunction<TaxiRide, EnrichedRideDemo> {

        @Override
        public void flatMap(TaxiRide value, Collector<EnrichedRideDemo> out) throws Exception {
            FilterFunction<TaxiRide> function = new RideCleansingSolution.NYCFilter();
            if (function.filter(value)) {
                out.collect(new EnrichedRideDemo());
            }
        }
    }

    public static class Enrichment implements MapFunction<TaxiRide, EnrichedRideDemo> {

        @Override
        public EnrichedRideDemo map(TaxiRide value) throws Exception {
            return new EnrichedRideDemo(value);
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Data
    public static class EnrichedRideDemo extends TaxiRide {
        public int startCell;
        public int endCell;

        public EnrichedRideDemo(TaxiRide ride) {
            this.rideId = ride.rideId;
            this.isStart = ride.isStart;
            this.eventTime = ride.eventTime;
            this.startLon = ride.startLon;
            this.startLat = ride.startLat;
            this.endLon = ride.endLon;
            this.endLat = ride.endLat;
            this.passengerCnt = ride.passengerCnt;
            this.taxiId = ride.taxiId;
            this.driverId = ride.driverId;

            this.startCell = GeoUtils.mapToGridCell(ride.startLon, ride.startLat);
            this.endCell = GeoUtils.mapToGridCell(ride.endLon, ride.endLat);

        }

    }
}
