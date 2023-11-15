package com.john.flink.demo.windowjoin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-11-15 23:04
 * @since jdk17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Goods {
    private String goodsId;
    private String goodsName;
    private BigDecimal goodsPrice;

    public static List<Goods> GOODS_LIST;
    public static Random r;

    static {
        r = new Random();

        GOODS_LIST = new ArrayList<>();

        GOODS_LIST.add(new Goods("1", "小米12", new BigDecimal(4890)));
        GOODS_LIST.add(new Goods("2", "iphone12", new BigDecimal(12000)));
        GOODS_LIST.add(new Goods("3", "MacBookPro", new BigDecimal(15000)));
        GOODS_LIST.add(new Goods("4", "Thinkpad X1", new BigDecimal(9800)));
        GOODS_LIST.add(new Goods("5", "MeiZu One", new BigDecimal(3200)));
        GOODS_LIST.add(new Goods("6", "Mate 40", new BigDecimal(6500)));
    }

    public static Goods randomGoods() {
        int rIndex = r.nextInt(GOODS_LIST.size());

        return GOODS_LIST.get(rIndex);
    }

}
