package com.john.flink.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-10-11 23:35
 * @since jdk17
 */
@Slf4j
public class DataStreamDemo1 {

    public static void main(String[] args) throws Exception {
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
        }
    }

}
