package com.john.flink.demo.window;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-12-11 23:18
 * @since jdk17
 */
public class SensorReadingSourceFunction extends RichSourceFunction<SensorReading> {

    private static final long serialVersionUID = -6869870597932157717L;
    private Boolean isCancel;

    @Override
    public void open(Configuration parameters) throws Exception {
        isCancel = false;
    }

    @Override
    public void run(SourceContext<SensorReading> ctx) throws Exception {
        while (BooleanUtils.isNotTrue(isCancel)) {
            List<SensorReading> goodsList = SensorReading.GOODS_LIST;
            goodsList.forEach(item -> ctx.collect(item));
            TimeUnit.SECONDS.sleep(10);
        }
    }

    @Override
    public void cancel() {
        isCancel = true;
    }
}
