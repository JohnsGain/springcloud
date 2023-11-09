package com.john.flink.ridecleanse.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.john.flink.common.dto.TaxiRide;
import com.john.flink.common.test.ComposedPipeline;
import com.john.flink.common.test.ExecutablePipeline;
import com.john.flink.common.test.ParallelTestSource;
import com.john.flink.common.test.TestSink;
import com.john.flink.ridecleanse.main.RideCleansingExercise;
import com.john.flink.ridecleanse.solution.RideCleansingSolution;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.runtime.testutils.MiniClusterResourceConfiguration;
import org.apache.flink.test.util.MiniClusterWithClientResource;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-10-20 00:48
 * @since jdk17
 */
public class RideCleansingIntegrationTest extends RideCleansingTestBase {

    private static final int PARALLELISM = 2;

    private static MiniClusterWithClientResource flinkCluster = new MiniClusterWithClientResource(
            new MiniClusterResourceConfiguration.Builder()
                    .setNumberSlotsPerTaskManager(PARALLELISM)
                    .setNumberTaskManagers(1)
                    .build());

    @Test
    public void testAMixtureOfLocations() throws Exception {
        TaxiRide toThePole = testRide(-73.9947F, 40.750626F, 0, 90);
        TaxiRide fromThePole = testRide(0, 90, -73.9947F, 40.750626F);
        TaxiRide atPennStation = testRide(-73.9947F, 40.750626F, -73.9947F, 40.750626F);
        TaxiRide atNorthPole = testRide(0, 90, 0, 90);
        ParallelTestSource<TaxiRide> source =
                new ParallelTestSource<>(toThePole, fromThePole, atPennStation, atNorthPole);
        TestSink<TaxiRide> sink = new TestSink<>();

        JobExecutionResult executionResult = rideCleansingPipeline().execute(source, sink);
        List<TaxiRide> rideList = sink.getResults(executionResult);
        System.out.println(new ObjectMapper().writeValueAsString(rideList));
        Assert.assertTrue(rideList.contains(atPennStation));
    }

    protected ComposedPipeline<TaxiRide, TaxiRide> rideCleansingPipeline() {
        ExecutablePipeline<TaxiRide, TaxiRide> exercise = (source, sink) -> new RideCleansingExercise(source, sink).execute();
        ExecutablePipeline<TaxiRide, TaxiRide> solution = (source, sink) -> new RideCleansingSolution(source, sink).execute();

        return new ComposedPipeline<>(exercise, solution);
    }
}
