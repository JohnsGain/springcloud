package com.john.flink.demo;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.MemorySize;
import org.apache.flink.connector.file.sink.FileSink;
import org.apache.flink.core.fs.Path;
import org.apache.flink.formats.parquet.avro.AvroParquetWriters;
import org.apache.flink.streaming.api.functions.sink.filesystem.OutputFileConfig;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.OnCheckpointRollingPolicy;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * https://nightlies.apache.org/flink/flink-docs-release-1.15/zh/docs/connectors/datastream/filesystem/#:~:
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2023-10-28 14:37
 * @since jdk17
 */
public class FileSinkDemo {

    /**
     * File Sink 将传入的数据写入存储桶中。考虑到输入流可以是无界的，每个桶中的数据被组织成有限大小的 Part 文件。
     * 完全可以配置为基于时间的方式往桶中写入数据，比如可以设置每个小时的数据写入一个新桶中。这意味着桶中将包含一个
     * 小时间隔内接收到的记录。
     * <p>
     * 桶目录中的数据被拆分成多个 Part 文件。对于相应的接收数据的桶的 Sink 的每个 Subtask，每个桶将至少包含一个
     * Part 文件。将根据配置的滚动策略来创建其他 Part 文件。 对于 Row-encoded Formats（参考 Format Types）
     * 默认的策略是根据 Part 文件大小进行滚动，需要指定文件打开状态最长时间的超时以及文件关闭后的非活动状态的超时
     * 时间。 对于 Bulk-encoded Formats 在每次创建 Checkpoint 时进行滚动，并且用户也可以添加基于大小或者时间
     * 等的其他条件。
     */
    @Test
    public void rawFormat() {
//        FileSink 不仅支持 Row-encoded 也支持 Bulk-encoded，例如 Apache Parquet。 这两种格式可以通过如下的静态方法进行构造：
//
//        Row-encoded sink: FileSink.forRowFormat(basePath, rowEncoder)
//        Bulk-encoded sink: FileSink.forBulkFormat(basePath, bulkWriterFactory)
        String dirPath = "/Users/zhangjuwa/Documents/javaProject/springcloud/flink/src/main/java/com/john/flink/demo";
        Path path = new Path(dirPath);
       /* 指定了滚动策略，当满足以下三个条件的任何一个时都会将 In-progress 状态文件进行滚动：
        包含了至少15分钟的数据量
         从没接收延时5分钟之外的新纪录
        文件大小已经达到 1MB（写入最后一条记录之后）*/
        DefaultRollingPolicy<String, String> rollingPolicy = DefaultRollingPolicy.builder()
                .withRolloverInterval(Duration.ofMinutes(15))
                .withInactivityInterval(Duration.ofMinutes(15))
                .withMaxPartSize(MemorySize.ofMebiBytes(1))
                .build();
        SimpleStringEncoder<String> simpleStringEncoder = new SimpleStringEncoder<>(StandardCharsets.UTF_8.name());
        FileSink<String> fileSink = FileSink.forRowFormat(path, simpleStringEncoder)
                .withRollingPolicy(rollingPolicy)
                .build();

    }

    /**
     * Bulk-encoded 的 Sink 的创建和 Row-encoded 的相似，但不需要指定 Encoder，而是需要指定 BulkWriter.Factory，
     * 请参考文档 BulkWriter.Factory 。 BulkWriter 定义了如何添加和刷新新数据以及如何最终确定一批记录使用哪种编码字符集的逻辑。
     * Flink 内置了5种 BulkWriter 工厂类：
     *
     * ParquetWriterFactory
     * AvroWriterFactory
     * SequenceFileWriterFactory
     * CompressWriterFactory
     * OrcBulkWriterFactory
     */
    @Test
    public void bulkFormat4Avro() {
//        重要: Bulk-encoded Format 仅支持一种继承了 CheckpointRollingPolicy 类的滚动策略。
//        在每个 Checkpoint 都会滚动。另外也可以根据大小或处理时间进行滚动。
        Schema schema = null;  // todo xx
        String dirPath = "/Users/zhangjuwa/Documents/javaProject/springcloud/flink/src/main/java/com/john/flink/demo";
        Path path = new Path(dirPath);
//        FileSink 写入 Parquet Format 的 Avro 数据：
        FileSink<GenericRecord> fileSink = FileSink.forBulkFormat(path, AvroParquetWriters.forGenericRecord(schema))
                .build();
    }

    @Test
    public void bulkFormat4Protobuf() {
        // ProtoRecord 是一个生成 protobuf 的类
        String dirPath = "/Users/zhangjuwa/Documents/javaProject/springcloud/flink/src/main/java/com/john/flink/demo";
        Path path = new Path(dirPath);

//        FileSink<IN> fileSink = FileSink.forBulkFormat(path, ParquetProtoWriters.forType(ProtoRecord.class))
//                .build();
    }
    @Test
    public void withOutputFileConfig() {
        String dirPath = "/Users/zhangjuwa/Documents/javaProject/springcloud/flink/src/main/java/com/john/flink/demo";
        Path path = new Path(dirPath);

        OutputFileConfig config = OutputFileConfig
                .builder()
                .withPartPrefix("prefix")
                .withPartSuffix(".ext")
                .build();

        FileSink<Tuple2<Integer, Integer>> sink = FileSink
                .forRowFormat(path, new SimpleStringEncoder<Tuple2<Integer, Integer>>("UTF-8"))
//                .withBucketAssigner(new KeyBucketAssigner())
                .withRollingPolicy(OnCheckpointRollingPolicy.build())
                .withOutputFileConfig(config)
                .build();
    }
}
