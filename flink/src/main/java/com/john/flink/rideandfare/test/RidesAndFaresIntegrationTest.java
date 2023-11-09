package com.john.flink.rideandfare.test;

import com.john.flink.common.dto.RideAndFare;
import com.john.flink.common.dto.TaxiFare;
import com.john.flink.common.dto.TaxiRide;
import com.john.flink.common.test.ComposedTwoInputPipeline;
import com.john.flink.common.test.ExecutableTwoInputPipeline;
import com.john.flink.common.test.ParallelTestSource;
import com.john.flink.common.test.TestSink;
import com.john.flink.rideandfare.main.RidesAndFaresExercise;
import org.apache.flink.runtime.testutils.MiniClusterResourceConfiguration;
import org.apache.flink.test.util.MiniClusterWithClientResource;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-11-10 01:34
 * @since jdk17
 */
public class RidesAndFaresIntegrationTest extends RidesAndFaresTestBase {

    private static final int PARALLELISM = 2;


    /**
     * This isn't necessary, but speeds up the tests.
     */
    @ClassRule
    public static MiniClusterWithClientResource flinkCluster = new
            MiniClusterWithClientResource(new MiniClusterResourceConfiguration.Builder()
            .setNumberSlotsPerTaskManager(PARALLELISM)
            .setNumberTaskManagers(1)
            .build()
    );

    @Test
    public void testSeveralRidesAndFaresMixedTogether() throws Exception {

        final TaxiRide ride1 = testRide(1);
        final TaxiFare fare1 = testFare(1);

        final TaxiRide ride2 = testRide(2);
        final TaxiFare fare2 = testFare(2);

        final TaxiRide ride3 = testRide(3);
        final TaxiFare fare3 = testFare(3);

        final TaxiRide ride4 = testRide(4);
        final TaxiFare fare4 = testFare(4);

        ParallelTestSource<TaxiRide> rides = new ParallelTestSource<>(ride1, ride4, ride3, ride2);
        ParallelTestSource<TaxiFare> fares = new ParallelTestSource<>(fare2, fare4, fare1, fare3);

        TestSink<RideAndFare> testSink = new TestSink<>();


    }

    protected ComposedTwoInputPipeline<TaxiRide, TaxiFare, RideAndFare> ridesAndFaresPipeline() {
        ExecutableTwoInputPipeline<TaxiRide, TaxiFare, RideAndFare> exercise = (a, b, c) ->
                new RidesAndFaresExercise(a, b, c)
                        .execute();

        ExecutableTwoInputPipeline<TaxiRide, TaxiFare, RideAndFare> solution = (a, b, c) -> {
            return null;
        };

        return new ComposedTwoInputPipeline<>(exercise, solution);
    }
}
