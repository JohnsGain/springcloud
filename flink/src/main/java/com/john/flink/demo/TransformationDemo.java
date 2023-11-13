package com.john.flink.demo;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.*;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.junit.Test;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-11-14 00:01
 * @since jdk17
 */
public class TransformationDemo {

//    最常用的有 map flatmap keyBy filter

//    A type cannot be a key if:
//
//    it is a POJO type but does not override the hashCode() method and relies on the Object.hashCode() implementation.
//    it is an array of any type.

    /**
     * 适用于 已经分组的 KeyedStream.
     * 用来做分组之后对每个组的聚合
     */
    @Test
    public void reduce() throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Person> frommedElements = environment.fromElements(getList(20));

        KeyedStream<Person, String> keyedBy = frommedElements.keyBy(Person::getCountry);
        SingleOutputStreamOperator<Person> reduce = keyedBy.reduce(new ReduceFunction<Person>() {
            @Override
            public Person reduce(Person value1, Person value2) throws Exception {
//                Person person = new Person(value1.name + value2.name, value1.age + value2.age);
//                person.setCountry(value1.country);
                Integer age1 = value1.age;
                Integer age2 = value2.getAge();

                return age1 >= age2 ? value1 : value2;
            }
        });

        reduce.print();

        environment.execute();
    }

    /**
     * 用于 KeyedStream
     * 对已经分组的事件  再按照 事件的特点进行分组,如：按照事件时间窗口分成不同的组
     * {@link  WindowWordCount#main(String[])}
     */
    @Test
    public void window() {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> socketedTextStream = environment.socketTextStream("localhost", 9999);
        SingleOutputStreamOperator<Tuple2<String, Integer>> flattedMap = socketedTextStream.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
                String[] split = value.split(" ");
                for (String item : split) {
                    out.collect(new Tuple2<>(item, 1));
                }
            }
        });


        KeyedStream<Tuple2<String, Integer>, String> keyedBy = flattedMap.keyBy(item -> item.f0);
        WindowedStream<Tuple2<String, Integer>, String, TimeWindow> window = keyedBy.window(
                TumblingProcessingTimeWindows.of(Time.seconds(5)));

//        window.
    }

    /**
     * window() 用于 KeyedStream, 若要对所有数据流
     * 按照 事件的特性进行分chunk [分块] 处理，就用windowAll
     */
    @Test
    public void windowAll() {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> socketedTextStream = environment.socketTextStream("localhost", 9999);
        AllWindowedStream<String, TimeWindow> windowedAll = socketedTextStream.windowAll(TumblingEventTimeWindows.of(Time.seconds(5)));

    }

    private Person[] getList(int size) {
        Person[] objects = new Person[size];
        String[] arr = {"US", "CN", "CA", "GE", "UK"};
        for (int i = 0; i < size; i++) {
            Person person = new Person();
            objects[i] = person;
            person.setCountry(arr[RandomUtils.nextInt(0, arr.length)]);
            person.setAge(RandomUtils.nextInt(0, 10));
            person.setName(RandomStringUtils.random(5, "ASDFGJHK1234578"));
        }
        return objects;
    }

}
