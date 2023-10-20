package com.shulian.netty.udp;

import lombok.Getter;

import java.net.InetSocketAddress;

/**
 * In messaging applications, data is often represented by a POJO, which may hold configuration
 * or processing information in addition to the actual message content. In this
 * application we’ll handle a message as an event, and because the data comes from a log
 * file, we’ll call it LogEvent. Listing 14.1 shows the details of this simple POJO.
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2020-09-29 15:46
 * @since jdk1.8
 */
@Getter
public class LogEvent {

    public static final byte SEPARATOR = (byte) ':';
    private final InetSocketAddress source;
    /**
     * 文件名
     */
    private final String logfile;
    /**
     * 事件消息内容
     */
    private final String msg;
    /**
     * 接收消息那一刻的时间戳
     */
    private final long received;

    public LogEvent(String logfile, String msg) {
        this(null, -1, logfile, msg);
    }

    public LogEvent(InetSocketAddress source, long received,
                    String logfile, String msg) {
        this.source = source;
        this.logfile = logfile;
        this.msg = msg;
        this.received = received;
    }


}
