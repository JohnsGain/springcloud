package com.john.flink.ridecleanse.test;

import com.john.flink.common.TaxiRide;
import com.john.flink.common.test.ComposedFilterFunction;
import com.john.flink.ridecleanse.main.RideCleansingExercise;
import com.john.flink.ridecleanse.solution.RideCleansingSolution;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-10-24 01:11
 * @since jdk17
 */
public class RideCleansingUnitTest {

    public ComposedFilterFunction filterFunction() {
        return new ComposedFilterFunction(new RideCleansingExercise.NYCFilter(), new RideCleansingSolution.NYCFilter());
    }

    @Test
    public void test() throws Exception {
        TaxiRide atPennStation = RideCleansingTestBase.testRide(-73.9947F, 40.750626F, -73.9947F, 40.750626F);
        assertThat(filterFunction().filter(atPennStation)).isTrue();
    }

    @Test
    public void testRideThatStartsOutsideNYC() throws Exception {

        TaxiRide fromThePole = RideCleansingTestBase.testRide(0, 90, -73.9947F, 40.750626F);
        assertThat(filterFunction().filter(fromThePole)).isFalse();
    }

    @Test
    public void testRideThatEndsOutsideNYC() throws Exception {

        TaxiRide toThePole = RideCleansingTestBase.testRide(-73.9947F, 40.750626F, 0, 90);
        assertThat(filterFunction().filter(toThePole)).isFalse();
    }

    @Test
    public void testRideThatStartsAndEndsOutsideNYC() throws Exception {

        TaxiRide atNorthPole = RideCleansingTestBase.testRide(0, 90, 0, 90);
        assertThat(filterFunction().filter(atNorthPole)).isFalse();
    }

}
