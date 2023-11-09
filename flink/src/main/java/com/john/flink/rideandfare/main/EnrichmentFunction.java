package com.john.flink.rideandfare.main;

import com.john.flink.common.MissingSolutionException;
import com.john.flink.common.dto.RideAndFare;
import com.john.flink.common.dto.TaxiFare;
import com.john.flink.common.dto.TaxiRide;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.co.RichCoFlatMapFunction;
import org.apache.flink.util.Collector;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-11-10 01:19
 * @since jdk17
 */
public class EnrichmentFunction extends RichCoFlatMapFunction<TaxiRide, TaxiFare, RideAndFare> {

    @Override
    public void open(Configuration parameters) throws Exception {
        throw new MissingSolutionException();
    }

    @Override
    public void flatMap1(TaxiRide value, Collector<RideAndFare> out) throws Exception {
        throw new MissingSolutionException();
    }

    @Override
    public void flatMap2(TaxiFare value, Collector<RideAndFare> out) throws Exception {
        throw new MissingSolutionException();
    }

}
