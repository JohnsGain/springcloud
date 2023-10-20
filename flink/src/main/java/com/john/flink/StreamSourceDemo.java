package com.john.flink;

import org.apache.flink.connector.file.src.FileSource;
import org.apache.flink.connector.file.src.reader.TextLineInputFormat;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-10-17 02:55
 * @since jdk17
 */
public class StreamSourceDemo {

    @Test
    public void fileSource() {
        List<Person> people = new ArrayList<Person>();

        people.add(new Person("Fred", 35));
        people.add(new Person("Wilma", 35));
        people.add(new Person("Pebbles", 2));

        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Person> frommedCollection = environment.fromCollection(people);


        // Another convenient way to get some data into a stream while prototyping is to use a socket
        DataStreamSource<String> localhost = environment.socketTextStream("localhost", 9999);

        // or a file
//        environment.readTextFile()


    }

    @Test
    public void test() {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        //        environment.readTextFile()
        FileSource<String> source = FileSource.forRecordStreamFormat(new TextLineInputFormat(),
                new Path("/Users/zhangjuwa/Documents/javaProject/springcloud/README.md")).build();


    }

}
