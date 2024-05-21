package com.john.flink.demo.connector;

import lombok.Data;

/**
 * 策略 操作日志
 *
 * @author zhangjuwa
 * @date 2023/7/19 18:16
 * @since jdk17
 */
@Data
public class StrategyExecutorLogKafkaMessage {

    /**
     * 主键ID，利用redis 生成
     */
    private Long id;

    private String createTime;

    /**
     * 策略id
     */
    private Long strategyId;
    /**
     * 策略操作日志 产生的服务
     */
    private String serverName;
    private Integer businessType;
    /**
     * 操作日志 变更内容
     */
    private String content;
    /**
     * 创建人名称
     */
    private String createName;

    /**
     * 创建人
     */
    private Long createBy;

}
