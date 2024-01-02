package com.john.flink.hourlytips.main;

import com.john.flink.common.dto.TaxiFare;
import com.john.flink.common.source.TaxiFareGenerator;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.time.Duration;

/**
 * The result of this exercise is a data stream of `Tuple3<Long, Long, Float>` records, one for each hour.
 * Each hourly record should contain the timestamp at the end of the hour, the driverId of the driver
 * earning the most in tips during that hour, and the actual total of their tips.
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2023-12-12 00:56
 * @since jdk17
 */
public class HourlyTipsExercise {

    private final SourceFunction<TaxiFare> source;

    private final SinkFunction<Tuple3<Long, Long, Double>> sink;

    /**
     * Creates a job using the source and sink provided.
     */
    public HourlyTipsExercise(
            SourceFunction<TaxiFare> source, SinkFunction<Tuple3<Long, Long, Double>> sink) {

        this.source = source;
        this.sink = sink;
    }

    public static void main(String[] args) throws Exception {
        TaxiFareGenerator taxiFareGenerator = new TaxiFareGenerator();
        SinkFunction<Tuple3<Long, Long, Double>> sinkFunction = new PrintSinkFunction<>();
        HourlyTipsExercise tipsExercise = new HourlyTipsExercise(taxiFareGenerator, sinkFunction);
        tipsExercise.execute();
    }

    /**
     * Create and execute the hourly tips pipeline.
     * The result of this exercise is a data stream of `Tuple3<Long, Long, Float>` records, one for each hour.
     * Each hourly record should contain the timestamp at the end of the hour,
     * the driverId of the driver earning the most in tips during that hour, and the actual total of their tips.
     *
     * @return {JobExecutionResult}
     * @throws Exception which occurs during job execution.
     */
    public JobExecutionResult execute() throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        WatermarkStrategy<TaxiFare> timestampAssigner = WatermarkStrategy.<TaxiFare>forBoundedOutOfOrderness(Duration.ofSeconds(10))
                .withTimestampAssigner((element, timestapm) -> element.getEventTimeMillis());

        SingleOutputStreamOperator<Tuple3<Long, Long, Double>> reduce = environment.addSource(source)
                .assignTimestampsAndWatermarks(timestampAssigner)
                .keyBy(TaxiFare::getDriverId)
                .window(TumblingEventTimeWindows.of(Time.hours(1)))

                .reduce(new SumFareReduce())
                .windowAll(TumblingEventTimeWindows.of(Time.hours(1)))
                .reduce(new MaxFareReduce(), new FareWindowAllFuntion());
        reduce.addSink(sink);

        return environment.execute("Hourly Tips");

    }

    private static class SumFareReduce implements ReduceFunction<TaxiFare> {

        @Override
        public TaxiFare reduce(TaxiFare value1, TaxiFare value2) throws Exception {
            float sumTip = value1.getTip() + value2.getTip();
            value2.setTip(sumTip);
            return value2;
        }
    }

    private static class MaxFareReduce implements ReduceFunction<TaxiFare> {

        @Override
        public TaxiFare reduce(TaxiFare value1, TaxiFare value2) throws Exception {
            return value1.getTip() > value2.getTip() ? value1 : value2;
        }
    }

    private static class FareWindowFuntion extends ProcessWindowFunction<
            TaxiFare, Tuple3<Long, Long, Double>, Long, TimeWindow> {
        @Override
        public void process(Long key, ProcessWindowFunction<TaxiFare, Tuple3<Long, Long, Double>, Long, TimeWindow>.Context context, Iterable<TaxiFare> elements, Collector<Tuple3<Long, Long, Double>> out) throws Exception {
            TaxiFare sum = elements.iterator().next();

            out.collect(new Tuple3<>(key, context.window().getEnd(), (double) sum.getTip()));
        }
    }


    private static class FareWindowAllFuntion extends ProcessAllWindowFunction<TaxiFare, Tuple3<Long, Long, Double>, TimeWindow> {

        @Override
        public void process(ProcessAllWindowFunction<TaxiFare, Tuple3<Long, Long, Double>, TimeWindow>.Context context, Iterable<TaxiFare> elements, Collector<Tuple3<Long, Long, Double>> out) throws Exception {
            TaxiFare max = elements.iterator().next();
            out.collect(new Tuple3<>(context.window().getEnd(), max.getDriverId(), (double) max.getTip()));
        }
    }


    public static class MyTipsMax extends ProcessWindowFunction<TaxiFare,                  // input type
            Tuple3<Long, Long, Double>,  // output type
            Long,                         // key type
            TimeWindow> {                   // window type

        @Override
        public void process(
                Long key,
                Context context,
                Iterable<TaxiFare> events,
                Collector<Tuple3<Long, Long, Double>> out) {

            double max = 0;
            for (TaxiFare event : events) {
                max = Math.max(event.getTip(), max);
            }
            out.collect(Tuple3.of(key, context.window().getEnd(), max));
        }
    }

}



