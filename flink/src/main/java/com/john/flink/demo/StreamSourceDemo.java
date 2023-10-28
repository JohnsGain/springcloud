package com.john.flink.demo;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.configuration.MemorySize;
import org.apache.flink.connector.file.sink.FileSink;
import org.apache.flink.connector.file.src.FileSource;
import org.apache.flink.connector.file.src.reader.TextLineInputFormat;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.IterativeStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
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

    /**
     * 在此示例中，我们创建了一个 DataStream，其中包含作为字符串的文本文件的行。 此处不需要水印策略，因为记录不包含事件时间戳。
     */
    @Test
    public void readTextFile() throws Exception {
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

        // 写到一个文件下面
        fileDataSource.sinkTo(getFileSink());

        System.out.println("开始执行");
        environment.execute();
    }

    /**
     * Iterative streaming programs implement a step function and embed it into an IterativeStream.
     * As a DataStream program may never finish, there is no maximum number of iterations. Instead,
     * you need to specify which part of the stream is fed back to the iteration and which part is
     * forwarded downstream using a side output or a filter. Here, we show an example using filters.
     * First, we define an IterativeStream
     */
    @Test
    public void iterativeStream() {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        Path path = new Path("/Users/zhangjuwa/Documents/javaProject/springcloud/README.md");
        TextLineInputFormat inputFormat = new TextLineInputFormat();
        FileSource<String> fileSource = FileSource.forRecordStreamFormat(inputFormat, path).build();
        DataStreamSource<String> input = environment.fromSource(fileSource, WatermarkStrategy.noWatermarks(), "README.md");
//
        IterativeStream<String> iterativeStream = input.iterate();
//    Then, we specify the logic that will be executed inside the loop using a series of transformations (here a simple map transformation)
    }

    private FileSink<String> getFileSink() {
        String dirPath = "/Users/zhangjuwa/Documents/javaProject/springcloud/flink/src/main/java/com/john/flink/demo";
        Path path = new Path(dirPath);
        SimpleStringEncoder<String> simpleStringEncoder = new SimpleStringEncoder<>(StandardCharsets.UTF_8.name());
        DefaultRollingPolicy<String, String> rollingPolicy = DefaultRollingPolicy.builder()
                .withInactivityInterval(Duration.ofSeconds(30))
                .withRolloverInterval(Duration.ofSeconds(30))
                .withMaxPartSize(MemorySize.ofMebiBytes(1))
                .build();
        return FileSink.forRowFormat(path, simpleStringEncoder)
                .withRollingPolicy(rollingPolicy)
                .build();
    }


}
