package com.john.flink.demo.window;

import com.john.flink.demo.Person;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.junit.Test;

import java.time.Duration;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-12-07 23:57
 * @since jdk17
 */
public class WatermarkStrategyDemo {

    /**
     * This is precisely what watermarks do — they define when to stop waiting for earlier events.
     * https://nightlies.apache.org/flink/flink-docs-master/docs/learn-flink/streaming_analytics/
     */
    @Test
    public void watermarkStrategy() {
        WatermarkStrategy<Person> watermarkStrategy = WatermarkStrategy.<Person>forBoundedOutOfOrderness(Duration.ofSeconds(20))
                .withTimestampAssigner((event, timestamp) -> event.getEventTimestamp());
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        Set<Person> collect = IntStream.range(1, 100).boxed()
                .map(item -> {
                    Person person = new Person();
                    person.setName(RandomStringUtils.random(8, "SADFGHJK12345678"));
                    person.setCountry("US");
                    person.setAge(RandomUtils.nextInt(0, 100));
                    return person;
                })
                .collect(Collectors.toSet());
        DataStreamSource<Person> dataStreamSource = environment.fromCollection(collect);
        dataStreamSource.assignTimestampsAndWatermarks(watermarkStrategy);
    }

    /**
     * 6种 Window Assigners
     * 滚动窗口：时间对齐，窗口长度固定，没有重叠。
     * 滑动窗口：
     * 会话窗口： 会话窗口不重叠，没有固定的开始和结束时间。一个session窗口通过一个session间隔来配置，
     * 这个session间隔定义了非活跃周期的长度，当这个非活跃周期产生，那么当前的session将关闭并且后续的元素将被分配到新的session窗口中去
     */
    @Test
    public void windowAssigners() {
//  Tumbling time windows :  page views per minute  滚动窗口，窗口是固定的
        TumblingEventTimeWindows tumblingEventTimeWindows = TumblingEventTimeWindows.of(Time.seconds(1));
// Sliding time windows: page views per minute computed every 10 seconds  滑动窗口
        SlidingEventTimeWindows slidingEventTimeWindows = SlidingEventTimeWindows.of(Time.minutes(1), Time.seconds(10));

// Session windows : page views per session, where sessions are defined by a gap of at least 30 minutes between sessions
        // 如果有30min 非活跃时间，后面的事件将进入一个新的会话窗口
        EventTimeSessionWindows eventTimeSessionWindows = EventTimeSessionWindows.withGap(Time.minutes(30));
    }
}
