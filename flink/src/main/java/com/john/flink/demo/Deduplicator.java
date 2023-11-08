package com.john.flink.demo;

import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.StateTtlConfig;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-11-09 00:01
 * @since jdk17
 */
public class Deduplicator extends RichFlatMapFunction<Person, Person> {


    /**
     * 用于存储 和 每个 事件key对应的状态
     */
    ValueState<Boolean> keyHasBeenSeen;

    @Override
    public void open(Configuration parameters) throws Exception {
        ValueStateDescriptor<Boolean> desc = new ValueStateDescriptor<>("keyHasBeenSeen", Types.BOOLEAN);
        // 可以为key 设置存活时间, 避免 key的缓存数据一直保存
        StateTtlConfig ttlConfig = StateTtlConfig.newBuilder(Time.minutes(5))
                .build();
        desc.enableTimeToLive(ttlConfig);
        keyHasBeenSeen = super.getRuntimeContext().getState(desc);
    }

    @Override
    public void flatMap(Person value, Collector<Person> out) throws Exception {
        if (keyHasBeenSeen.value() == null) {
            out.collect(value);
            keyHasBeenSeen.update(true);
        }
    }
}
