package com.john.flink.common.source;

import com.john.flink.common.DataGenerator;
import com.john.flink.common.dto.TaxiFare;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.time.Duration;
import java.time.Instant;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-11-10 01:22
 * @since jdk17
 */
public class TaxiFareGenerator implements SourceFunction<TaxiFare> {

    private volatile boolean isRunning = true;
    private Instant limitingTimestamp = Instant.MAX;


    /** Create a bounded TaxiFareGenerator that runs only for the specified duration. */
    public static TaxiFareGenerator runFor(Duration duration) {
        TaxiFareGenerator generator = new TaxiFareGenerator();
        generator.limitingTimestamp = DataGenerator.BEGINNING.plusMillis(duration.toMillis());
        return generator;
    }

    @Override
    public void run(SourceContext<TaxiFare> ctx) throws Exception {
        long id = 1;
        while (isRunning) {
            TaxiFare fare = new TaxiFare(id);

            // don't emit events that exceed the specified limit
            if (fare.getStartTime().compareTo(limitingTimestamp) >= 0) {
                break;
            }
            ++id;
            ctx.collect(fare);
            // match our event production rate to that of the TaxiRideGenerator
            Thread.sleep(TaxiRideGenerator.SLEEP_MILLIS_PER_EVENT);
        }
    }

    @Override
    public void cancel() {
        isRunning = false;
    }
}
