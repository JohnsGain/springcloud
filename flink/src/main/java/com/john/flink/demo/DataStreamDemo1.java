package com.john.flink.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.LocalStreamEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.CloseableIterator;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-10-11 23:35
 * @since jdk17
 */
@Slf4j
public class DataStreamDemo1 {

    @Test
    public void basicUsage() {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        try (env) {
            DataStreamSource<Person> dataStreamSource = env.fromElements(
                    new Person("Fred", 35),
                    new Person("Wilma", 35),
                    new Person("Pebbles", 2),
                    new Person("Jimmy", 5)
            );
            SingleOutputStreamOperator<Person> adults = dataStreamSource.filter(item -> item.age > 18);

            adults.print();
            System.out.println("开始执行");
            env.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * flink 各种执行环境配置变量
     * https://www.bookstack.cn/read/flink-1.16-zh/981261b942af04c3.md
     */
    @Test
    public void envParam() {
        Configuration configuration = new Configuration();
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        environment.setBufferTimeout(500);
    }

    /**
     * 在创建它的同一个 JVM 进程中启动 Flink 系统。如果你从 IDE 启动 LocalEnvironment，则可以在代码中设置断点并轻松调试程序。
     */
    @Test
    public void localEnv() throws Exception {
        LocalStreamEnvironment localEnvironment = StreamExecutionEnvironment.createLocalEnvironment();
        DataStreamSource<Long> frommedSequence = localEnvironment.fromSequence(0, 100);

        SingleOutputStreamOperator<String> streamOperator = frommedSequence.map(item -> item + "666");
        CloseableIterator<String> collectedAsync = streamOperator.collectAsync();

    }

    @Test
    public void collectionSource() {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
// 从元素列表创建一个 DataStream
        DataStream<Integer> myInts = env.fromElements(1, 2, 3, 4, 5);
// 从任何 Java 集合创建一个 DataStream
        List<Tuple2<String, Integer>> data = List.of(new Tuple2<>("", 1));
        DataStream<Tuple2<String, Integer>> myTuples = env.fromCollection(data);
// 从迭代器创建一个 DataStream
        Iterator<Long> longIt = List.of(1L, 6L, 9L).iterator();
        DataStream<Long> myLongs = env.fromCollection(longIt, Long.class);
    }

}
