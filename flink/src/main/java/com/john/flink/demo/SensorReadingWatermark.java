package com.john.flink.demo;

import org.apache.flink.api.common.eventtime.*;

import java.time.Duration;
import java.time.Instant;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-12-11 23:30
 * @since jdk17
 */
public class SensorReadingWatermark implements WatermarkStrategy<SensorReading> {

    @Override
    public TimestampAssigner<SensorReading> createTimestampAssigner(TimestampAssignerSupplier.Context context) {
        return ((element, recordTimestamp) -> Instant.now().toEpochMilli());
    }

    @Override
    public WatermarkGenerator<SensorReading> createWatermarkGenerator(WatermarkGeneratorSupplier.Context context) {
        return new BoundedOutOfOrdernessWatermarks<>(Duration.ofSeconds(5));
    }
}
