package com.john.flink.demo.window;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-12-11 23:16
 * @since jdk17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorReading {

    private String name;
    private Integer value;

    private long timestamp;


    public static List<SensorReading> GOODS_LIST;
    public static Random r;

    static {
        r = new Random();

        GOODS_LIST = new ArrayList<>();

        GOODS_LIST.add(new SensorReading("小米12", RandomUtils.nextInt(0, 1000), Instant.now().toEpochMilli()));
        GOODS_LIST.add(new SensorReading("iphone12", RandomUtils.nextInt(0, 1000), Instant.now().toEpochMilli()));
        GOODS_LIST.add(new SensorReading("MacBookPro", RandomUtils.nextInt(0, 1000), Instant.now().toEpochMilli()));
        GOODS_LIST.add(new SensorReading("Thinkpad X1", RandomUtils.nextInt(0, 1000), Instant.now().toEpochMilli()));
        GOODS_LIST.add(new SensorReading("MeiZu One", RandomUtils.nextInt(0, 1000), Instant.now().toEpochMilli()));
        GOODS_LIST.add(new SensorReading("Mate 40", RandomUtils.nextInt(0, 1000), Instant.now().toEpochMilli()));
    }

}
