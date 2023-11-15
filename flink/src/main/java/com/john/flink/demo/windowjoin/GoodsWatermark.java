package com.john.flink.demo.windowjoin;

import org.apache.flink.api.common.eventtime.*;

import java.time.Instant;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-11-15 22:58
 * @since jdk17
 */
public class GoodsWatermark implements WatermarkStrategy<Goods> {

    @Override
    public TimestampAssigner<Goods> createTimestampAssigner(TimestampAssignerSupplier.Context context) {
        return (element, recordTimestamp) -> Instant.now().toEpochMilli();
    }

    @Override
    public WatermarkGenerator<Goods> createWatermarkGenerator(WatermarkGeneratorSupplier.Context context) {
        return new WatermarkGenerator<Goods>() {
            @Override
            public void onEvent(Goods event, long eventTimestamp, WatermarkOutput output) {
                output.emitWatermark(new Watermark(Instant.now().toEpochMilli()));
            }

            @Override
            public void onPeriodicEmit(WatermarkOutput output) {
                output.emitWatermark(new Watermark(Instant.now().toEpochMilli()));
            }
        };
    }
}
