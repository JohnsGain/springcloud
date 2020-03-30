package com.shulian.netty.test;

import lombok.Data;

/**
 * @author Lee HN
 * @date 2019/6/18 13:53
 */
@Data
public class NettyProperties {

    private String strategy;

    private int tcpPort;

    private int udpPort;

    private int frameLength;

    private String delimiter;

}
