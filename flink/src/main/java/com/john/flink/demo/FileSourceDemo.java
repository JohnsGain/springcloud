package com.john.flink.demo;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.file.src.FileSource;
import org.apache.flink.connector.file.src.reader.TextLineInputFormat;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.junit.Test;

/**
 * https://nightlies.apache.org/flink/flink-docs-release-1.15/zh/docs/connectors/datastream/filesystem/#:~:
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2023-10-28 16:31
 * @since jdk17
 */
public class FileSourceDemo {

    @Test
    public void txtFile() {
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
    }

    @Test
    public void csvFileSource() {
        Path path = new Path("/Users/zhangjuwa/Documents/javaProject/springcloud/xxx.csv");
//        final FileSource<String> source =
//                FileSource.forRecordStreamFormat(new PojoCsvInputFormat<Person>(path, null))
//        .monitorContinuously(Duration.ofMillis(5))
//                .build();
    }
}
