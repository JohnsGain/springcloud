package com.john.flink.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-11-10 01:08
 * @since jdk17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideAndFare implements Serializable {

    private static final long serialVersionUID = -3486178593614185271L;
    private TaxiRide ride;

    private TaxiFare fare;
}
