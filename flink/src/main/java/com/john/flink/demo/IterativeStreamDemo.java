package com.john.flink.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.IterativeStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-10-31 00:19
 * @since jdk17
 */
public class IterativeStreamDemo {

    public static void main(String[] args) throws Exception {
        // 指定运行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<Long> frommedSequence = env.fromSequence(0, 1000);
        SingleOutputStreamOperator<IterationNum> operator = frommedSequence.map(item -> new IterationNum(item, item, 0L));
        IterativeStream<IterationNum> iterativeStream = operator.iterate();
        // 做数据的map处理， 就是value - 1
        SingleOutputStreamOperator<IterationNum> minusOne = iterativeStream.map(item -> {
            item.setValue(item.getValue() - 1);
            item.setIterationNum(item.getIterationNum() + 1);
            return item;
        });
        // 大于0的数据是需要重复迭代的
        SingleOutputStreamOperator<IterationNum> stillGreaterThanZero = minusOne.filter(item -> item.getValue() > 0);
        iterativeStream.closeWith(stillGreaterThanZero);

        // <= 0的数据是要输出的
        SingleOutputStreamOperator<IterationNum> lessThanZero = minusOne.filter(item -> item.getValue() <= 0);
        System.out.println("小于0的");
        lessThanZero.print();
        env.execute("test iterativeStream");
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class IterationNum {
        private Long originValue;
        private Long value;

        private Long iterationNum;
    }
}
