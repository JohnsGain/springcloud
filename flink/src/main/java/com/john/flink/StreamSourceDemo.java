package com.john.flink;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
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
    public void collectionSource() {
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
    public void fileSource() {
        Path path = new Path("/Users/zhangjuwa/Documents/javaProject/springcloud/xxx.csv");
//        final FileSource<String> source =
//                FileSource.forRecordStreamFormat(new PojoCsvInputFormat<Person>(path, null))
//        .monitorContinuously(Duration.ofMillis(5))
//                .build();
    }

    /**
     * 在此示例中，我们创建了一个 DataStream，其中包含作为字符串的文本文件的行。 此处不需要水印策略，因为记录不包含事件时间戳。
     */
    @Test
    public void readTextFile() throws Exception {

        // 从文件流中读取文件内容
//        FileSource.forRecordStreamFormat(StreamFormat,Path...);

// 从文件中一次读取一批记录
//        FileSource.forBulkFileFormat(BulkFormat,Path...);

        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        TextLineInputFormat inputFormat = new TextLineInputFormat();
        Path path = new Path("/Users/zhangjuwa/Documents/javaProject/springcloud/README.md");
        FileSource<String> source = FileSource.forRecordStreamFormat(inputFormat, path).build();
        DataStreamSource<String> fileDataSource = environment.fromSource(source, WatermarkStrategy.noWatermarks(),
                "README.md");
        // 使用标准输出sink PrintSinkFunction
//        fileDataSource.print();

        // 先 得到每行字符的 长度 ，再打印  PrintSinkFunction
//        fileDataSource.map((MapFunction<String, Integer>) String::length)
//                .print();

        // 写到一个文件下面  todo
//        fileDataSource.addSink(new FileSink())

        System.out.println("开始执行");
        environment.execute();
    }

    @Test
    public void readCsvFile() {

    }

    @Test
    public void test() {
        //        environment.readTextFile()
        FileSource<String> source = FileSource.forRecordStreamFormat(new TextLineInputFormat(),
                new Path("/Users/zhangjuwa/Documents/javaProject/springcloud/README.md")).build();


    }

}
