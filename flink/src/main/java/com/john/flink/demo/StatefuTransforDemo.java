package com.john.flink.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomUtils;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-11-09 00:51
 * @since jdk17
 */
public class StatefuTransforDemo {

    /**
     * 去重
     */
    @Test
    public void duplicate() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        List<Person> peopleL = getPerList(10);
        System.out.println(objectMapper.writeValueAsString(peopleL));
        DataStreamSource<Person> frommedCollection = environment.fromCollection(peopleL);
        // 按国家分组
        KeyedStream<Person, String> keyedBy = frommedCollection.keyBy(Person::getCountry);
        // 相同国家里面 ,只保留第一个出现的国家 事件
        keyedBy.flatMap(new Deduplicator())
                .print();

        environment.execute();
    }

    @Test
    public void connectedStream() throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        KeyedStream<String, String> controlStream = environment.fromElements("DROP", "IGNORE")
                .keyBy(item -> item);

        KeyedStream<String, String> streamOfWords = environment.fromElements("Apache", "xxx", "DROP", "Flink", "IGNORE", "xx")
                .keyBy(item -> item);

        // connected
        controlStream.connect(streamOfWords)
                .flatMap(new ControlFunction())
                .print();

        environment.execute();
    }

    private List<Person> getPerList(int size) {
        var list = new ArrayList<Person>();
        String[] countryArr = {"US", "UK", "CN"};
        for (int i = 0; i < size; i++) {
            Person person = new Person();
            list.add(person);
            person.setAge(i % 3);
            person.setName("name" + i);
            person.setCountry(countryArr[RandomUtils.nextInt(0, 3)]);
        }
        return list;
    }

}
