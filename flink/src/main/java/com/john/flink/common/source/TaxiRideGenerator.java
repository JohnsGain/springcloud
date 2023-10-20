package com.john.flink.common.source;

import com.john.flink.common.TaxiRide;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.*;

/**
 * This SourceFunction generates a data stream of TaxiRide records.
 *
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
        PriorityQueue<TaxiRide> endEventQ = new PriorityQueue<>(100);
        long id = 0;
        long maxStartTime = 0;
        while (running) {
            // generate a batch of START events
            List<TaxiRide> startEvents = new ArrayList<TaxiRide>(BATCH_SIZE);
            for (int i = 1; i <= BATCH_SIZE; i++) {
                TaxiRide ride = new TaxiRide(i + 1, true);
                startEvents.add(ride);
                maxStartTime = Math.max(maxStartTime, ride.getEventTimeMillis());
            }
            for (int i = 1; i <= BATCH_SIZE; i++) {
                TaxiRide ride = new TaxiRide(i + 1, false);
                endEventQ.add(ride);
            }

            while (true) {
                assert endEventQ.peek() != null;
                if (!(endEventQ.peek().getEventTimeMillis() <= maxStartTime)) {
                    break;
                }
                TaxiRide end = endEventQ.poll();
                ctx.collect(end);
            }

            Collections.shuffle(startEvents, new Random(id));
            startEvents.iterator()
                    .forEachRemaining(ctx::collect);
            // prepare for the next batch
            id += BATCH_SIZE;

            // don't go too fast
            Thread.sleep(BATCH_SIZE * SLEEP_MILLIS_PER_EVENT);
        }

    }

    @Override
    public void cancel() {
        running = false;
    }

}
