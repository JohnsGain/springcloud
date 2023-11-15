package com.john.flink.demo.windowjoin;

import lombok.Data;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-11-15 23:04
 * @since jdk17
 */
@Data
public class OrderItem {
    private String itemId;
    private String goodsId;
    private Integer count;
}
