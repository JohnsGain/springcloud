package com.shulian.cloud;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimerService;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.walkthrough.common.entity.Alert;
import org.apache.flink.walkthrough.common.entity.Transaction;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2024-01-02 21:38
 * @since jdk17
 */
public class FraudDetector extends KeyedProcessFunction<Long, Transaction, Alert> {


    private static final double SMALL_AMOUNT = 1.00;
    private static final double LARGE_AMOUNT = 500.00;
    private static final long ONE_MINUTE = 60 * 1000;

    private transient ValueState<Boolean> flagState;

    private transient ValueState<Long> timerState;

    @Override
    public void open(Configuration parameters) throws Exception {
        ValueStateDescriptor<Boolean> descriptor = new ValueStateDescriptor<>("flat", Types.BOOLEAN);
        flagState = getRuntimeContext().getState(descriptor);

        ValueStateDescriptor<Long> timeDes = new ValueStateDescriptor<>("timer-state", Types.LONG);
        timerState = getRuntimeContext().getState(timeDes);
    }

    @Override
    public void processElement(Transaction value, KeyedProcessFunction<Long, Transaction, Alert>.Context ctx, Collector<Alert> out) throws Exception {
        Boolean lastTransactionWasSmall = flagState.value();
        double amount = value.getAmount();
        if (BooleanUtils.isTrue(lastTransactionWasSmall) && amount >= LARGE_AMOUNT) {
            Alert alert = new Alert();
            alert.setId(value.getAccountId());
            out.collect(alert);
            cleanUp(ctx);
            return;
        }
        if (amount < SMALL_AMOUNT) {
            flagState.update(true);
            long currentProcessingTime = ctx.timerService().currentProcessingTime();
            long timer = currentProcessingTime + ONE_MINUTE;
            ctx.timerService().registerProcessingTimeTimer(timer);
            timerState.update(timer);
        }
    }

    /**
     * When a timer fires, it calls KeyedProcessFunction#onTimer. Overriding this method is how you can
     * implement your callback to reset the flag.
     */
    @Override
    public void onTimer(long timestamp, KeyedProcessFunction<Long, Transaction, Alert>.OnTimerContext ctx, Collector<Alert> out) throws Exception {
        // remove flag after 1 minute
        flagState.clear();
        timerState.clear();
    }

    private void cleanUp(Context ctx) throws Exception {
        TimerService timerService = ctx.timerService();
        Long value = timerState.value();
        timerService.deleteProcessingTimeTimer(value);
        flagState.clear();
        timerState.clear();
    }
}
