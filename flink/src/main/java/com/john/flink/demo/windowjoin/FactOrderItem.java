package com.john.flink.demo.windowjoin;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-11-15 23:03
 * @since jdk17
 */
@Data
public class FactOrderItem {
    private String goodsId;
    private String goodsName;
    private BigDecimal count;
    private BigDecimal totalMoney;
}
