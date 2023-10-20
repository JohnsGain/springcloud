package com.john.flink;

import org.apache.flink.api.java.tuple.Tuple2;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-10-17 02:43
 * @since jdk17
 */
public class FlinkTuple {

    public static void main(String[] args) {
        Tuple2<String, Integer> tuple2 = Tuple2.of("hhh", 666);
        String f0 = tuple2.f0;
        Integer f1 = tuple2.f1;
    }
}
