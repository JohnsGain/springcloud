package com.shulian.netty;

import lombok.Data;

/**
 * 多个提供商
 *
 * @author lee
 * @date 2018年11月12日
 */
@Data
public class SmsTemplate {

    private String ali;

    private int tencent;

}
