package com.john.flink.demo.window;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * Here are examples of approaches 1 and 3. Each implementation finds the peak value from each sensor in
 * 1 minute event time windows, and producing a stream of Tuples containing (key, end-of-window-timestamp, max_value).
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2023-12-11 23:23
 * @since jdk17
 */

public class MyWastefulMax extends ProcessWindowFunction<SensorReading, Tuple3<String, Long, Integer>, String, TimeWindow> {
    private static final long serialVersionUID = 7839314505370499030L;

    /**
     * Here are examples of approaches 1 and 3. Each implementation finds the peak value from each sensor in 1 minute event time windows, and producing a stream of Tuples containing (key, end-of-window-timestamp, max_value).
     *
     * @param key      The key for which this window is evaluated.
     * @param context  The context in which the window is being evaluated.
     * @param elements The elements in the window being evaluated.
     * @param out      A collector for emitting elements.
     * @throws Exception
     */
    @Override
    public void process(String key, ProcessWindowFunction<SensorReading, Tuple3<String, Long, Integer>, String, TimeWindow>.Context context, Iterable<SensorReading> elements, Collector<Tuple3<String, Long, Integer>> out) throws Exception {
        int max = 0;
        for (SensorReading element : elements) {
            max = Math.max(max, element.getValue());
        }
        out.collect(new Tuple3<>(key, context.window().getEnd(), max));
    }
}
