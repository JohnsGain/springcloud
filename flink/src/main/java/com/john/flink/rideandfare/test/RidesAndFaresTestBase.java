package com.john.flink.rideandfare.test;

import com.john.flink.common.dto.TaxiFare;
import com.john.flink.common.dto.TaxiRide;
import org.junit.Before;

import java.time.Instant;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-11-10 01:34
 * @since jdk17
 */
public class RidesAndFaresTestBase {

    @Before
    public void before() {
        System.out.println("beforexxx");
    }

    public static TaxiRide testRide(long rideId) {
        return new TaxiRide(rideId, true, Instant.EPOCH, 0F, 0F, 0F, 0F, (short) 1, 0, rideId);
    }

    public static TaxiFare testFare(long rideId) {
        return new TaxiFare(rideId, 0, rideId, Instant.EPOCH, "", 0F, 0F, 0F);
    }

}
