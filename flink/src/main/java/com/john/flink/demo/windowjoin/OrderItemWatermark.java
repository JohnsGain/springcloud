package com.john.flink.demo.windowjoin;

import org.apache.flink.api.common.eventtime.*;

import java.time.Instant;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-11-15 23:01
 * @since jdk17
 */
public class OrderItemWatermark implements WatermarkStrategy<OrderItem> {
    private static final long serialVersionUID = -5567544446105069541L;

    @Override
    public TimestampAssigner<OrderItem> createTimestampAssigner(TimestampAssignerSupplier.Context context) {
        return (element, recordTimestamp) -> Instant.now().toEpochMilli();
    }

    @Override
    public WatermarkGenerator<OrderItem> createWatermarkGenerator(WatermarkGeneratorSupplier.Context context) {
        return new WatermarkGenerator<OrderItem>() {
            @Override
            public void onEvent(OrderItem event, long eventTimestamp, WatermarkOutput output) {
                output.emitWatermark(new Watermark(Instant.now().toEpochMilli()));
            }

            @Override
            public void onPeriodicEmit(WatermarkOutput output) {
                output.emitWatermark(new Watermark(Instant.now().toEpochMilli()));
            }
        };
    }
}
