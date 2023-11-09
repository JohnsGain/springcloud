package com.john.flink.rideandfare.solution;

import com.john.flink.common.dto.RideAndFare;
import com.john.flink.common.dto.TaxiFare;
import com.john.flink.common.dto.TaxiRide;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.co.RichCoFlatMapFunction;
import org.apache.flink.util.Collector;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-11-10 02:27
 * @since jdk17
 */
public class EnrichmentFunction4Solution extends RichCoFlatMapFunction<TaxiRide, TaxiFare, RideAndFare> {

    @Override
    public void open(Configuration parameters) throws Exception {

    }

    @Override
    public void close() throws Exception {
        super.close();
    }

    @Override
    public void flatMap1(TaxiRide value, Collector<RideAndFare> out) throws Exception {

    }

    @Override
    public void flatMap2(TaxiFare value, Collector<RideAndFare> out) throws Exception {

    }
}
