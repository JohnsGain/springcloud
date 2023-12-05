package com.john.flink.demo;

import com.john.flink.demo.windowjoin.OrderItem;
import com.john.flink.demo.windowjoin.OrderItemRichSourceFunction;
import com.john.flink.demo.windowjoin.WindowJoinDemo;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.CoGroupFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.Partitioner;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
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

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
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

                return new Person(value1.name + value2.name, value1.age + value2.age, value1.country);
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
     * 若要对所有数据流
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
     * 用于 进行windowAll()函数转换过的 AllWindowedStream 进行apply 运算
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
     * @see WindowJoinDemo
     */
    @Test
    public void windowJoin() throws Exception {
//        WindowJoinDemo
    }

    /**
     * KeyedStream,KeyedStream → DataStream #
     * Join two elements e1 and e2 of two keyed streams with a common key over a given time interval,
     * so that e1.timestamp + lowerBound <= e2.timestamp <= e1.timestamp + upperBound.
     *
     * @throws Exception
     */
    @Test
    public void intervalJoin() throws Exception {
// this will join the two streams so that
// key1 == key2 && leftTs - 2 < rightTs < leftTs + 2
//        keyedStream.intervalJoin(otherKeyedStream)
//                .between(Time.milliseconds(-2), Time.milliseconds(2)) // lower and upper bound
//                .upperBoundExclusive(true) // optional
//                .lowerBoundExclusive(true) // optional
//                .process(new IntervalJoinFunction() {...});
    }

    /**
     * DataStream,DataStream → DataStream #
     * Cogroups two data streams on a given key and a common window.
     * 与join有区别，不管key有没有关联上,最终都会合并成一个数据流
     */
    @Test
    public void windowCoGroup() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        List<Tuple3<String, String, Integer>> tuple3List1 = Arrays.asList(
                new Tuple3<>("OWEN", "girl", 18),
                new Tuple3<>("伍七", "girl", 22),
                new Tuple3<>("TOM", "man", 29),
                new Tuple3<>("ALEN", "middle", 25)
        );

        List<Tuple3<String, String, Integer>> tuple3List2 = Arrays.asList(
                new Tuple3<>("伍六", "girl", 18),
                new Tuple3<>("JOHN", "man", 21),
                new Tuple3<>("吴八", "man", 26)
        );

        SingleOutputStreamOperator<Tuple3<String, String, Integer>> dataStream1 = env.fromCollection(tuple3List1)
                //添加水印窗口,如果不添加，则时间窗口会一直等待水印事件时间，不会执行apply
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy.<Tuple3<String, String, Integer>>forBoundedOutOfOrderness(Duration.ofSeconds(5))
                                .withTimestampAssigner((element, recordTimestamp) -> Instant.now().toEpochMilli())
                );

        SingleOutputStreamOperator<Tuple3<String, String, Integer>> dataStream2 = env.fromCollection(tuple3List2)
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy.<Tuple3<String, String, Integer>>forBoundedOutOfOrderness(Duration.ofSeconds(5))
                                .withTimestampAssigner((element, recordTimestamp) -> Instant.now().toEpochMilli())
                );

        //对dataStream1和dataStream2两个数据流进行关联，没有关联也保留
        DataStream<String> coGroupStream = dataStream1.coGroup(dataStream2)
                .where(item -> item.f1)
                .equalTo(item -> item.f1)
                .window(TumblingEventTimeWindows.of(Time.seconds(5)))
                .apply(new CoGroupFunction<Tuple3<String, String, Integer>, Tuple3<String, String, Integer>, String>() {
                    @Override
                    public void coGroup(Iterable<Tuple3<String, String, Integer>> first, Iterable<Tuple3<String, String, Integer>> second, Collector<String> out) throws Exception {
                        StringBuilder sb = new StringBuilder();
                        //datastream1的数据流集合
                        for (Tuple3<String, String, Integer> tuple3 : first) {
                            sb.append(tuple3.f0).append("-");
                        }
                        //datastream2的数据流集合
                        for (Tuple3<String, String, Integer> tuple3 : second) {
                            sb.append(tuple3.f0).append("-");
                        }
                        out.collect(sb.toString());
                    }
                });

        coGroupStream.print();

        env.execute();
    }

    /**
     * DataStream,DataStream → ConnectedStream #
     * “Connects” two data streams retaining their types. Connect allowing for shared state between the two streams.
     *
     * @see {@link StatefuTransforDemo#connectedStream()}
     */
    @Test
    public void connectedStream() {

    }

    /**
     * CoMap, CoFlatMap #
     * ConnectedStream → DataStream #
     * Similar to map and flatMap on a connected data stream
     *
     * @see {@link StatefuTransforDemo#connectedStream()}
     */
    @Test
    public void coMapAndCoFlatMap() {

    }

    /**
     * ataStream → IterativeStream → ConnectedStream #
     *
     * @see {@link IterativeStreamDemo}
     */
    @Test
    public void iterate() {

    }

    /**
     * DataStream → CachedDataStream #
     * Cache the intermediate result of the transformation. Currently, only jobs that run with batch execution mode
     * are supported. The cache intermediate result is generated lazily at the first time the intermediate result
     * is computed so that the result can be reused by later jobs. If the cache is lost, it will be recomputed
     * using the original transformations.
     */
    @Test
    public void cache() {

    }

    //  ============== Physical Partitioning   ==============

    /**
     * Custom Partitioning
     */
    @Test
    public void customPartitioning() {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<OrderItem> addedSource = environment.addSource(new OrderItemRichSourceFunction());
        DataStream<OrderItem> someKey = addedSource.partitionCustom(new Partitioner<Object>() {
            @Override
            public int partition(Object key, int numPartitions) {
                return 0;
            }
        }, "someKey");
    }

    /**
     * DataStream → DataStream
     * Partitions elements randomly according to a uniform distribution.
     */
    @Test
    public void randomPartitioning() {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<OrderItem> addedSource = environment.addSource(new OrderItemRichSourceFunction());
        DataStream<OrderItem> shuffle = addedSource.shuffle();

    }

    /**
     *
     */
    @Test
    public void rescaling() {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<OrderItem> addedSource = environment.addSource(new OrderItemRichSourceFunction());
        DataStream<OrderItem> rescale = addedSource.rescale();

    }

    /**
     * DataStream → DataStream #
     * Broadcasts elements to every partition.
     */
    @Test
    public void broadcasting() {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<OrderItem> addedSource = environment.addSource(new OrderItemRichSourceFunction());
        DataStream<OrderItem> broadcast = addedSource.broadcast();
    }

    private Person[] getList(int size) {
        Person[] objects = new Person[size];
        String[] arr = {"US", "CN", "CA", "GE", "UK"};
        for (int i = 0; i < size; i++) {
            Person person = new Person();
            objects[i] = person;
            person.setCountry(arr[RandomUtils.nextInt(0, arr.length)]);
            person.setAge(RandomUtils.nextInt(0, 100));
            person.setName(RandomStringUtils.random(5, "ASDFGJHK1234578"));
        }
        return objects;
    }

}
