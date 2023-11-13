package com.john.flink.rideandfare.solution;

import com.john.flink.common.dto.RideAndFare;
import com.john.flink.common.dto.TaxiFare;
import com.john.flink.common.dto.TaxiRide;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
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

    private ValueState<TaxiRide> rideValueState;

    private ValueState<TaxiFare> fareValueState;


    @Override
    public void open(Configuration parameters) throws Exception {
        RuntimeContext runtimeContext = getRuntimeContext();
        ValueStateDescriptor<TaxiRide> rideValue = new ValueStateDescriptor<>("ride value", TaxiRide.class);
        rideValueState = runtimeContext.getState(rideValue);

        ValueStateDescriptor<TaxiFare> fareValue = new ValueStateDescriptor<>("fare value", TaxiFare.class);
        fareValueState = runtimeContext.getState(fareValue);
    }

    @Override
    public void close() throws Exception {
        super.close();
    }

    @Override
    public void flatMap1(TaxiRide value, Collector<RideAndFare> out) throws Exception {
        TaxiFare fare = fareValueState.value();
        if (fare != null) {
            out.collect(new RideAndFare(value, fare));
            fareValueState.clear();
        } else {
            rideValueState.update(value);
        }
    }

    @Override
    public void flatMap2(TaxiFare value, Collector<RideAndFare> out) throws Exception {
        TaxiRide ride = rideValueState.value();
        if (ride != null) {
            out.collect(new RideAndFare(ride, value));
            rideValueState.clear();
        } else {
            fareValueState.update(value);
        }
    }
}
