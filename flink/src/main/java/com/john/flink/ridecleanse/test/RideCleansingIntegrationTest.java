package com.john.flink.ridecleanse.test;

import com.john.flink.common.TaxiRide;
import com.john.flink.common.test.ParallelTestSource;
import org.apache.flink.runtime.testutils.MiniClusterResourceConfiguration;
import org.apache.flink.test.util.MiniClusterWithClientResource;
import org.junit.Test;

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

    }
}
