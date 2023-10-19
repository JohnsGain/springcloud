package com.john.flink.ridecleanse.test;

import com.john.flink.common.TaxiRide;

import java.time.Instant;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-10-20 00:43
 * @since jdk17
 */
public class RideCleansingTestBase {

    public static TaxiRide testRide(float startLon, float startLat, float endLon, float endLat) {
        return new TaxiRide(
                1L, true, Instant.EPOCH, startLon, startLat, endLon, endLat, (short) 1, 0, 0);
    }

}
