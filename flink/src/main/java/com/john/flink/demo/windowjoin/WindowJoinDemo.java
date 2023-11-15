package com.john.flink.demo.windowjoin;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.math.BigDecimal;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-11-15 22:31
 * @since jdk17
 */
public class WindowJoinDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        // 构建商品数据流
        SingleOutputStreamOperator<Goods> goodsSource = environment.addSource(new GoodsRichSourceFunction(), TypeInformation.of(Goods.class))
                .assignTimestampsAndWatermarks(new GoodsWatermark());
        // 构建订单明细数据流
        SingleOutputStreamOperator<OrderItem> orderItemSource = environment.addSource(new OrderItemRichSourceFunction())
                .assignTimestampsAndWatermarks(new OrderItemWatermark());
        // 进行关联查询
        DataStream<FactOrderItem> apply = goodsSource.join(orderItemSource)
                .where(Goods::getGoodsId)
                .equalTo(OrderItem::getGoodsId)
                .window(TumblingEventTimeWindows.of(Time.seconds(5)))
                .apply((good, order) -> {
                    FactOrderItem factOrderItem = new FactOrderItem();
                    factOrderItem.setGoodsId(good.getGoodsId());
                    factOrderItem.setGoodsName(good.getGoodsName());
                    factOrderItem.setCount(new BigDecimal(order.getCount()));
                    factOrderItem.setTotalMoney(good.getGoodsPrice().multiply(factOrderItem.getCount()));
                    return factOrderItem;
                });

        apply.print();
        environment.execute("滚动窗口JOIN");

    }

}
