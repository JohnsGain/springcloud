package com.john.flink.demo;


import com.john.flink.common.GeoUtils;
import com.john.flink.common.TaxiRide;
import com.john.flink.common.source.TaxiRideGenerator;
import com.john.flink.ridecleanse.solution.RideCleansingSolution;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
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
