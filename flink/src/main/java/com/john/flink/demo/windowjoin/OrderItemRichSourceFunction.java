package com.john.flink.demo.windowjoin;

import com.john.flink.common.SnowflakeIdGenerator;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-11-15 22:50
 * @since jdk17
 */
public class OrderItemRichSourceFunction extends RichSourceFunction<OrderItem> {

    private static final long serialVersionUID = 2281076768539342264L;
    private Boolean isCancel;
    private Random r;

    static SnowflakeIdGenerator snowflakeIdGenerator = new SnowflakeIdGenerator(1, 2);

    @Override
    public void run(SourceContext<OrderItem> ctx) throws Exception {
        while (!isCancel) {
            Goods goods = Goods.randomGoods();
            OrderItem orderItem = new OrderItem();
            orderItem.setGoodsId(goods.getGoodsId());
            orderItem.setCount(r.nextInt(10) + 1);
            orderItem.setItemId(snowflakeIdGenerator.nextId() + "");

            ctx.collect(orderItem);

            orderItem.setGoodsId("111");
            ctx.collect(orderItem);

            TimeUnit.SECONDS.sleep(1);
        }
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        isCancel = false;
        r = new Random();
    }

    @Override
    public void cancel() {
        isCancel = true;
    }
}
