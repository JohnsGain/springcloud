package com.john.flink;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-10-26 23:56
 * @since jdk17
 */
public class WindowWordCount {

    /**
     * 启动Main 之前，先在命令行执行命令： nc -lk 9999
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> socketedTextStream = environment.socketTextStream("localhost", 9999);
        SingleOutputStreamOperator<Tuple2<String, Integer>> operator = socketedTextStream.flatMap(new Splitter())
                .keyBy(key -> key.f0)
                .window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
                .sum(1);

        operator.print();

        environment.execute("Window WordCount");
    }


    public static class Splitter implements FlatMapFunction<String, Tuple2<String, Integer>> {

        @Override
        public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
            String[] split = value.split(" ");
            for (String item : split) {
                out.collect(new Tuple2<>(item, 1));
            }
        }
    }

}
