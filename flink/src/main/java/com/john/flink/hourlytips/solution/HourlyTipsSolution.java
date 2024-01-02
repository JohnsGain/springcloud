package com.john.flink.hourlytips.solution;

import com.john.flink.common.dto.TaxiFare;
import com.john.flink.common.source.TaxiFareGenerator;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-12-13 01:23
 * @since jdk17
 */
public class HourlyTipsSolution {

    private final SourceFunction<TaxiFare> sourceFunction;

    private final SinkFunction<Tuple3<Long, Long, Double>> sinkFunction;

    /**
     * Creates a job using the source and sink provided.
     */
    public HourlyTipsSolution(
            SourceFunction<TaxiFare> source, SinkFunction<Tuple3<Long, Long, Double>> sink) {

        this.sourceFunction = source;
        this.sinkFunction = sink;
    }


    /**
     * Main method.
     *
     * @throws Exception which occurs during job execution.
     */
    public static void main(String[] args) throws Exception {

        HourlyTipsSolution job =
                new HourlyTipsSolution(new TaxiFareGenerator(), new PrintSinkFunction<>());

        job.execute();
    }

    public JobExecutionResult execute() throws Exception {
        // set up streaming execution environment
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        SingleOutputStreamOperator<TaxiFare> watermarks = env.addSource(sourceFunction)
                .assignTimestampsAndWatermarks(WatermarkStrategy.<TaxiFare>forMonotonousTimestamps()
                        .withTimestampAssigner(((element, recordTimestamp) -> element.getEventTimeMillis())));

        SingleOutputStreamOperator<Tuple3<Long, Long, Double>> hourlyTips = watermarks.keyBy(item -> item.getDriverId())
                .window(TumblingEventTimeWindows.of(Time.hours(1)))
                .process(new FareSumWindowFuntion());

        // find the driver with the highest sum of tips for each hour
        SingleOutputStreamOperator<Tuple3<Long, Long, Double>> hourlyMax = hourlyTips.windowAll(TumblingEventTimeWindows.of(Time.hours(1)))
                .maxBy(2);

        /* You should explore how this alternative (commented out below) behaves.
         * In what ways is the same as, and different from, the solution above (using a windowAll)?
         */

        // DataStream<Tuple3<Long, Long, Float>> hourlyMax = hourlyTips.keyBy(t -> t.f0).maxBy(2);

        hourlyMax.addSink(sinkFunction);

        // execute the transformation pipeline
        return env.execute("Hourly Tips");
    }

    private static class FareSumWindowFuntion extends ProcessWindowFunction<
            TaxiFare, Tuple3<Long, Long, Double>, Long, TimeWindow> {
        @Override
        public void process(Long key, ProcessWindowFunction<TaxiFare, Tuple3<Long, Long, Double>, Long, TimeWindow>.Context context, Iterable<TaxiFare> elements, Collector<Tuple3<Long, Long, Double>> out) throws Exception {
            double max = 0;
            for (TaxiFare event : elements) {
                max += event.getTip();
            }
            out.collect(new Tuple3<>(context.window().getEnd(), key, max));
        }
    }

}
