package com.john.flink.demo.connector;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.mysql.cj.jdbc.MysqlXADataSource;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        offsets.put(new TopicPartition(TOPIC, 0), 3599L);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        KafkaSource<String> source = KafkaSource.<String>builder()
                .setBootstrapServers("10.158.158.31:9092")

                .setGroupId("my-group")
                // 设置这个就把kafka-source设置成 batchMode
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

        SingleOutputStreamOperator<StrategyExecutorLogKafkaMessage> streamOperator = kafkaSource.map((String s) -> {
            StrategyExecutorLogKafkaMessage value = INSTANCE.readValue(s, StrategyExecutorLogKafkaMessage.class);
            return value;

        });
//        kafkaSource.print();

        streamOperator.addSink(getSink());
        env.execute();

    }

    private static SinkFunction<StrategyExecutorLogKafkaMessage> getSink() {
        JdbcExecutionOptions executionOptions = JdbcExecutionOptions.builder()
                // optional: default = 0, meaning no time-based execution is done
                .withBatchIntervalMs(200)
                // optional: default = 5000 values
                .withBatchSize(1000)
                // optional: default = 3
                .withMaxRetries(5)
                .build();
        JdbcConnectionOptions connectionOptions = new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                .withUrl("jdbc:mysql://10.158.158.47:3306/test_zhcxkj_center_strategy?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&autoReconnect=true&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true")
                .withDriverName("com.mysql.cj.jdbc.Driver")
                .withUsername("rwuser")
                .withPassword("Zhcx?2024")
                .build();
        return JdbcSink.sink(
                "insert into t_strategy_execute_log( id,strategy_id,server_name,business_type,content," +
                        "create_by,create_name,create_time) values (?, ?, ?, ?,?, ?,?, ?)", (ps, log) -> {

                    ps.setLong(1, log.getId());
                    ps.setLong(2, log.getStrategyId());
                    ps.setString(3, log.getServerName());
                    ps.setInt(4, log.getBusinessType());
                    ps.setString(5, log.getContent());
                    ps.setLong(6, log.getCreateBy());
                    ps.setString(7, log.getCreateName());
                    ps.setString(8, log.getCreateTime());
                },
                executionOptions, connectionOptions);
    }

    private void dd() {
        MysqlXADataSource xaDataSource = new com.mysql.cj.jdbc.MysqlXADataSource();
        xaDataSource.setUrl("jdbc:mysql://10.10.55.42:9031,10.10.55.42:9032,10.10.55.42:9033/test_zhcxkj_center?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&autoReconnect=true&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true");
        xaDataSource.setUser("root");
        xaDataSource.setPassword("8_gUwMCkZynQqo4G");
    }


    private static final ObjectMapper INSTANCE = new ObjectMapper();

    static {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        INSTANCE.registerModule(javaTimeModule);
        INSTANCE.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}
