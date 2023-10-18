package com.john.flink.common.source;

import com.john.flink.common.TaxiRide;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

/**
 * This SourceFunction generates a data stream of TaxiRide records.
 * @author zhangjuwa
 * @apiNote
 * @date 2023-10-19 00:58
 * @since jdk17
 */
public class TaxiRideGenerator implements SourceFunction<TaxiRide> {

    public static final int SLEEP_MILLIS_PER_EVENT = 10;
    private static final int BATCH_SIZE = 5;
    private volatile boolean running = true;

    @Override
    public void run(SourceContext<TaxiRide> ctx) throws Exception {

    }

    @Override
    public void cancel() {

    }
}
