package com.john.flink.demo;

import com.john.flink.common.dto.TaxiFare;
import com.john.flink.common.source.TaxiFareGenerator;
import lombok.SneakyThrows;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimerService;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-12-19 22:36
 * @since jdk17
 */
public class SideOutputDemo {

    @SneakyThrows
    public static void main(String[] args) {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        SingleOutputStreamOperator<Tuple3<Long, Long, Double>> processed = environment.addSource(new TaxiFareGenerator())
                .assignTimestampsAndWatermarks(WatermarkStrategy.<TaxiFare>forMonotonousTimestamps()
                        .withTimestampAssigner(((element, recordTimestamp) -> element.getEventTimeMillis())))
                .keyBy(item -> item.getDriverId())
                .process(new KeyedProcessFuncWithLateEvent(Time.hours(1)));

        // 专门延迟事件做处理
        DataStreamSink<TaxiFare> print = processed.getSideOutput(KeyedProcessFuncWithLateEvent.lateFares)
                .print();

//        ...
        environment.execute();
    }


    private static class KeyedProcessFuncWithLateEvent extends KeyedProcessFunction<Long, TaxiFare, Tuple3<Long, Long, Double>> {

        public static final OutputTag<TaxiFare> lateFares = new OutputTag<>("lateFares") {
        };
        private MapState<Long, Double> sumTips;
        private final long durationMillis;

        public KeyedProcessFuncWithLateEvent(Time time) {
            durationMillis = time.toMilliseconds();
        }

        @Override
        public void onTimer(long timestamp, KeyedProcessFunction<Long, TaxiFare, Tuple3<Long, Long, Double>>.OnTimerContext ctx, Collector<Tuple3<Long, Long, Double>> out) throws Exception {
            Long currentKey = ctx.getCurrentKey();
            Double tip = sumTips.get(currentKey);
            out.collect(new Tuple3<>(currentKey, timestamp, tip));
            sumTips.remove(currentKey);
        }

        @Override
        public void open(Configuration parameters) throws Exception {
            MapStateDescriptor<Long, Double> descriptor = new MapStateDescriptor<>("sum of tips", Long.class, Double.class);
            sumTips = getRuntimeContext().getMapState(descriptor);
        }

        @Override
        public void processElement(TaxiFare value, KeyedProcessFunction<Long, TaxiFare, Tuple3<Long, Long, Double>>.Context ctx, Collector<Tuple3<Long, Long, Double>> out) throws Exception {
            long eventTimeMillis = value.getEventTimeMillis();

            TimerService timerService = ctx.timerService();
            long currentedWatermark = timerService.currentWatermark();
            if (eventTimeMillis <= currentedWatermark) {
                ctx.output(lateFares, value);
            } else {
                Long currentKey = ctx.getCurrentKey();
                Double tip = sumTips.get(currentKey);
                if (tip == null) {
                    tip = 0.0;
                }
                tip += value.getTip();
                sumTips.put(currentKey, tip);
            }
        }
    }

}
