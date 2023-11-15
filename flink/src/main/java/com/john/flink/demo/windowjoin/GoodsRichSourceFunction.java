package com.john.flink.demo.windowjoin;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-11-15 22:34
 * @since jdk17
 */
public class GoodsRichSourceFunction extends RichSourceFunction<Goods> {


    private static final long serialVersionUID = -3241081745306612279L;

    private Boolean isCancel;

    @Override
    public void open(Configuration parameters) throws Exception {
        isCancel = false;
    }

    @Override
    public void run(SourceContext<Goods> ctx) throws Exception {
        while (!isCancel) {
            List<Goods> goods = Goods.GOODS_LIST;
            goods.forEach(ctx::collect);
            TimeUnit.SECONDS.sleep(1);
        }
    }

    @Override
    public void cancel() {
        isCancel = true;
    }
}
