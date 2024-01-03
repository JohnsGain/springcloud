package com.john.flink.frauddetection;

import lombok.SneakyThrows;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.walkthrough.common.entity.Alert;
import org.apache.flink.walkthrough.common.entity.Transaction;
import org.apache.flink.walkthrough.common.source.TransactionSource;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2024-01-02 21:36
 * @since jdk17
 */
public class FraudDetectionJob {

    @SneakyThrows
    public static void main(String[] args) {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        SingleOutputStreamOperator<Transaction> transactions = environment.addSource(new TransactionSource())
                .name("transactions");

        SingleOutputStreamOperator<Alert> alerts = transactions.keyBy(Transaction::getAccountId)
                .process(new FraudDetector())
                .name("fraud-detector");

        alerts.print()
                .name("send-alerts");

        environment.execute("Fraud Detection");
    }
}
