package com.john.flink.demo.connector;

import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

/**
 * @author zhangjuwa
 * @date 2024/5/14 21:23
 * @since jdk17
 */
public class JdbcSinkDemo {

    public static void main(String[] args) {

        JdbcExecutionOptions executionOptions = JdbcExecutionOptions.builder()
                .withBatchIntervalMs(200)             // optional: default = 0, meaning no time-based execution is done
                .withBatchSize(1000)                  // optional: default = 5000 values
                .withMaxRetries(5)                    // optional: default = 3
                .build();
        JdbcConnectionOptions connectionOptions = new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                .withUrl("jdbc:mysql://10.10.55.42:9031,10.10.55.42:9032,10.10.55.42:9033/test_zhcxkj_center?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&autoReconnect=true&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true")
                .withDriverName("com.mysql.cj.jdbc.Driver")
                .withUsername("root")
                .withPassword("8_gUwMCkZynQqo4G")
                .build();

        SinkFunction<StrategyExecutorLogKafkaMessage> sink = JdbcSink.sink("insert into books (id, title, authors, year) values (?, ?, ?, ?)",
                (ps, book) -> {
                    ps.setLong(1, book.getId());
                    ps.setString(2, book.getContent());
                    ps.setString(3, book.getCreateName());
                    ps.setString(4, book.getCreateTime());
                }, executionOptions, connectionOptions);
    }
}
