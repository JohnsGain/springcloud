package com.john.shardingjdbc.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 接入平台方订单数据对应表
 * </p>
 *
 * @author auto-generator
 * @since 2021-05-07
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sharding_mod")
public class ShardingModEntity {

    private static final long serialVersionUID = 1L;

    public static final String ID = "id";
    public static final String ORDER_NO = "order_no";
    public static final String PLATFORM_ORDER_NO = "platform_order_no";
    public static final String PLATFORM_STATUS = "platform_status";
    public static final String SOURCE = "source";
    public static final String REMARK = "remark";
    public static final String CREATE_TIME = "create_time";
    public static final String UPDATE_TIME = "update_time";

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 内部订单号
     */
    private String orderNo;

    /**
     * 平台方订单号
     */
    private String platformOrderNo;

    /**
     * 平台方订单状态
     */
    private Integer platformStatus;

    /**
     * 平台方渠道编码，标识具体平台方
     */
    private String source;

    /**
     * 备注说明
     */
    private String remark;

    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    private BigDecimal discount;

}
