package com.john.flink.demo.connector;

import com.mysql.cj.jdbc.MysqlXADataSource;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author zhangjuwa
 * @date 2024/5/11 15:18
 * @since jdk17
 */
public class KafkaSourceDemo {


    public static final String TOPIC = "STRATEGY_EXECUTOR_LOG_TOPIC";

    public static void main(String[] args) throws Exception {
        final HashSet<TopicPartition> partitionSet = new HashSet<>(Arrays.asList(
                new TopicPartition(TOPIC, 0)));
        Map<TopicPartition, Long> offsets = new HashMap<>();
        offsets.put(new TopicPartition(TOPIC, 0), 3245L);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        KafkaSource<String> source = KafkaSource.<String>builder()
                .setBootstrapServers("10.158.158.31:9092")

                .setGroupId("my-group")
                .setBounded(OffsetsInitializer.offsets(offsets))
                /*
                In order to handle scenarios like topic scaling-out or topic creation without restarting the Flink job,
                Kafka source can be configured to periodically discover new partitions under provided topic-partition
                subscribing pattern. To enable partition discovery, set a non-negative value for property partition.discovery.interval.ms:
                 */
                .setProperty("partition.discovery.interval.ms", "10000")
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setPartitions(partitionSet)
                .setDeserializer(KafkaRecordDeserializationSchema.valueOnly(StringDeserializer.class))
                .build();

        DataStreamSource<String> kafkaSource = env.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka Source");

        kafkaSource.print();

        env.execute();

    }

    private void dd() {
        MysqlXADataSource xaDataSource = new com.mysql.cj.jdbc.MysqlXADataSource();
        xaDataSource.setUrl("jdbc:mysql://10.10.55.42:9031,10.10.55.42:9032,10.10.55.42:9033/test_zhcxkj_center?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&autoReconnect=true&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true");
        xaDataSource.setUser("root");
        xaDataSource.setPassword("8_gUwMCkZynQqo4G");
    }
}
