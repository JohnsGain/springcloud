package com.shulian.netty;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lee
 * @date 2018/11/8 16:15
 */
@Data
public class CommonProperties {

    private Map<String, SmsTemplate> smsScene = new HashMap<>();

    private Map<String, Boolean> registeredScene = new HashMap<>();

    private String tempFileLocation;

    private NettyProperties netty = new NettyProperties();

}
