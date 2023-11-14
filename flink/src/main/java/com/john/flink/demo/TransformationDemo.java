package com.john.flink.demo;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.*;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.junit.Test;

import java.util.List;

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

    /**
     * 用于 进行window()函数转换过的 WindowedStream 进行apply 运算
     */
    @Test
    public void windowFunction() throws Exception {
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
        WindowedStream<Tuple2<String, Integer>, String, TimeWindow> windowedStream = keyedBy.window(
                TumblingProcessingTimeWindows.of(Time.seconds(5)));
        SingleOutputStreamOperator<String> applyStream = windowedStream.apply(new WindowFunction<Tuple2<String, Integer>, String, String, TimeWindow>() {
            @Override
            public void apply(String s, TimeWindow window, Iterable<Tuple2<String, Integer>> values, Collector<String> out) throws Exception {
                StringBuffer sum = new StringBuffer();
                for (Tuple2<String, Integer> item : values) {
                    sum.append("-").append(item.f0);
                }
                out.collect(sum.toString());
            }
        });

        applyStream.print();

        environment.execute();
    }


    /**
     * 用于 进行windowAll()函数转换过的 WindowedStream 进行apply 运算
     * <p>
     * 下面函数用来计算 每个窗口时间之间 出现的事件数量
     */
    @Test
    public void windowAllFunction() throws Exception {
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

        AllWindowedStream<Tuple2<String, Integer>, TimeWindow> windowedAll = flattedMap.windowAll(
                TumblingProcessingTimeWindows.of(Time.seconds(5)));
        SingleOutputStreamOperator<Integer> apply = windowedAll.apply(new AllWindowFunction<Tuple2<String, Integer>, Integer, TimeWindow>() {
            @Override
            public void apply(TimeWindow window, Iterable<Tuple2<String, Integer>> values, Collector<Integer> out) throws Exception {
                int sum = 0;
                for (Tuple2<String, Integer> value : values) {
                    sum += value.f1;
                }
                out.collect(sum);
            }
        });

        apply.print();

        environment.execute();
    }

    /**
     * 用于 进行window()函数转换过的 WindowedStream 进行 reduce function
     *
     * @throws Exception
     */
    @Test
    public void windowReduce() throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> socketedTextStream = environment.socketTextStream("localhost", 9999);
        SingleOutputStreamOperator<Tuple2<String, Integer>> flattedMap = socketedTextStream.flatMap(new FlatMapFunction<>() {
            private static final long serialVersionUID = -3243435268249802604L;

            @Override
            public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
                String[] split = value.split(" ");
                for (String item : split) {
                    out.collect(new Tuple2<>(item, 1));
                }
            }
        });
        KeyedStream<Tuple2<String, Integer>, String> keyedBy = flattedMap.keyBy(item -> item.f0);
        WindowedStream<Tuple2<String, Integer>, String, TimeWindow> windowedStream = keyedBy.window(
                TumblingProcessingTimeWindows.of(Time.seconds(5)));

        SingleOutputStreamOperator<Tuple2<String, Integer>> reduced = windowedStream.reduce(new ReduceFunction<Tuple2<String, Integer>>() {
            @Override
            public Tuple2<String, Integer> reduce(Tuple2<String, Integer> value1, Tuple2<String, Integer> value2) throws Exception {
                return new Tuple2<>(value1.f0, value1.f1 + value2.f1);
            }
        });
        reduced.print();

        environment.execute();
    }

    /**
     * Union of two or more data streams creating a new stream containing all the elements from all the streams.
     * Note: If you union a data stream with itself you will get each element twice in the resulting stream
     *
     * @throws Exception
     */
    @Test
    public void union() throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Integer> first = environment.fromElements(1, 5, 9);
        DataStreamSource<Integer> second = environment.fromCollection(List.of(1, 6, 9, 3, 6, 6));
        DataStream<Integer> union = first.union(second);
        union.print();

        environment.execute();
    }

    /**
     * DataStream,DataStream → DataStream #
     * Join two data streams on a given key and a common window.
     *
     * @throws Exception
     */
    @Test
    public void windowJoin() throws Exception {

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
