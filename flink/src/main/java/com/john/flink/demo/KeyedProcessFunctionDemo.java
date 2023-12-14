package com.john.flink.demo;

import com.john.flink.common.dto.TaxiFare;
import com.john.flink.common.source.TaxiFareGenerator;
import com.john.flink.hourlytips.solution.HourlyTipsSolution;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimerService;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-12-15 00:46
 * @since jdk17
 */
public class KeyedProcessFunctionDemo {

    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        SingleOutputStreamOperator<TaxiFare> watermarks = env.addSource(new TaxiFareGenerator())
                .assignTimestampsAndWatermarks(WatermarkStrategy.<TaxiFare>forMonotonousTimestamps()
                        .withTimestampAssigner(((element, recordTimestamp) -> element.getEventTimeMillis())));

        SingleOutputStreamOperator<Tuple3<Long, Long, Double>> processed = watermarks.keyBy(item -> item.getDriverId())
                .process(new PseudoWindow(Time.hours(1)));

        // 后序处理...

    }

    /**
     * 使用  KeyedProcessFunction  替换 下面 的代码处理逻辑 {@link HourlyTipsSolution#execute()}
     * .window(TumblingEventTimeWindows.of(Time.hours(1)))
     * .process(new FareSumWindowFuntion());
     */
    private static class PseudoWindow extends KeyedProcessFunction<Long, TaxiFare, Tuple3<Long, Long, Double>> {

        private final long durationMsec;

        /**
         * 每个窗口有一个entry, entry的key 是这个窗口的最后时间值
         */
        // Keyed, managed state, with an entry for each window, keyed by the window's end time.
// There is a separate MapState object for each driver.
        private transient MapState<Long, Double> sumOfTips;

        public PseudoWindow(Time duration) {
            this.durationMsec = duration.toMilliseconds();
        }

        /**
         * // Called once during initialization.
         *
         * @param parameters The configuration containing the parameters attached to the contract.
         * @throws Exception
         */
        @Override
        public void open(Configuration parameters) throws Exception {
            MapStateDescriptor<Long, Double> sumDesc =
                    new MapStateDescriptor<>("sumOfTips", Long.class, Double.class);
            sumOfTips = getRuntimeContext().getMapState(sumDesc);
        }

        // Called when the current watermark indicates that a window is now complete.
        @Override
        public void onTimer(long timestamp, KeyedProcessFunction<Long, TaxiFare, Tuple3<Long, Long, Double>>.OnTimerContext ctx, Collector<Tuple3<Long, Long, Double>> out) throws Exception {
            super.onTimer(timestamp, ctx, out);
        }


        // Called as each fare arrives to be processed.
        @Override
        public void processElement(TaxiFare value, KeyedProcessFunction<Long, TaxiFare, Tuple3<Long, Long, Double>>.Context ctx, Collector<Tuple3<Long, Long, Double>> out) throws Exception {
            long eventTimeMillis = value.getEventTimeMillis();
            TimerService timerService = ctx.timerService();
            if (eventTimeMillis <= timerService.currentWatermark()) {
                // This event is late; its window has already been triggered.
            } else {
                // Round up eventTime to the end of the window containing this event.
            }
        }

    }


}
