package com.john.flink.common.test;

import com.john.flink.common.MissingSolutionException;
import com.john.flink.common.dto.TaxiRide;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-10-24 01:13
 * @since jdk17
 */
public class ComposedFilterFunction implements FilterFunction<TaxiRide> {

    private static Logger log = LogManager.getLogger(ComposedFilterFunction.class);
    private final FilterFunction<TaxiRide> exercise;
    private final FilterFunction<TaxiRide> solution;

    public ComposedFilterFunction(FilterFunction<TaxiRide> exercise, FilterFunction<TaxiRide> solution) {
        this.exercise = exercise;
        this.solution = solution;
    }

    @Override
    public boolean filter(TaxiRide value) throws Exception {
        boolean result;
        try {
            result = exercise.filter(value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            if (MissingSolutionException.ultimateCauseIsMissingSolution(e)) {
                result = solution.filter(value);
            } else {
                throw e;
            }
        }
        return result;
    }
}
