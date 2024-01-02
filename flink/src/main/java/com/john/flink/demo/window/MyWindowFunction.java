package com.john.flink.demo.window;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-12-11 23:53
 * @since jdk17
 */
public class MyWindowFunction extends ProcessWindowFunction<
        SensorReading, Tuple3<String, Long, SensorReading>, String, TimeWindow> {
    @Override
    public void process(String key, ProcessWindowFunction<SensorReading, Tuple3<String, Long, SensorReading>, String, TimeWindow>.Context context,
                        Iterable<SensorReading> elements, Collector<Tuple3<String, Long, SensorReading>> out) throws Exception {
//Notice that the Iterable<SensorReading> will contain exactly one reading – the pre-aggregated maximum computed by MyReducingMax.
        SensorReading max = elements.iterator().next();

        out.collect(new Tuple3<>(key, context.window().getEnd(), max));
    }
}
