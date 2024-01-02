package com.john.flink.demo.window;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.junit.Test;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-12-08 01:42
 * @since jdk17
 */
public class ProcessWindowFunctionDemo {

    @Test
    public void test() throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        SingleOutputStreamOperator<SensorReading> assignedTimestampsAndWatermarks = environment.addSource(new SensorReadingSourceFunction(), TypeInformation.of(SensorReading.class))
                .assignTimestampsAndWatermarks(new SensorReadingWatermark());

        KeyedStream<SensorReading, String> keyedStream = assignedTimestampsAndWatermarks.keyBy(SensorReading::getName);

        WindowedStream<SensorReading, String, TimeWindow> window = keyedStream.window(TumblingEventTimeWindows.of(Time.seconds(30)));

        SingleOutputStreamOperator<Tuple3<String, Long, Integer>> process = window.process(new MyWastefulMax());

        process.print();

        environment.execute("测试 窗口函数");
    }

    /**
     * @throws Exception
     */
    @Test
    public void incrementalAggregation() throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        SingleOutputStreamOperator<SensorReading> assignedTimestampsAndWatermarks = environment.addSource(new SensorReadingSourceFunction(), TypeInformation.of(SensorReading.class))
                .assignTimestampsAndWatermarks(new SensorReadingWatermark());

        KeyedStream<SensorReading, String> keyedStream = assignedTimestampsAndWatermarks.keyBy(SensorReading::getName);

        WindowedStream<SensorReading, String, TimeWindow> window = keyedStream.window(TumblingEventTimeWindows.of(Time.seconds(30)));

        SingleOutputStreamOperator<Tuple3<String, Long, SensorReading>> reduce = window.reduce(new MyReducingMax(), new MyWindowFunction());

        reduce.print();

        environment.execute("测试 窗口函数");
    }

}
