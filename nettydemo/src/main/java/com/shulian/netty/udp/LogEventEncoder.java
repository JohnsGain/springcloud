package com.shulian.netty.udp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * As you’ve seen, all data to be transmitted is encapsulated in LogEvent messages.
 * The LogEventBroadcasterServer writes these to the channel, sending them through the
 * ChannelPipeline where they’re converted (encoded) into DatagramPacket messages.
 * Finally, they are broadcast via UDP and picked up by remote peers (monitors).
 * The next listing shows our customized version of MessageToMessageEncoder, which
 * performs the conversion just described.
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2020-09-29 16:55
 * @since jdk1.8
 */
@Slf4j
public class LogEventEncoder extends MessageToMessageEncoder<LogEvent> {

    /**
     * 接收端的网络地址，255.255.255.255表示本地局域网内所有主机都可以接受到，并且不会被路由器转发到其他 网络
     */
    private final InetSocketAddress remoteAddress;

    public LogEventEncoder(InetSocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, LogEvent msg, List<Object> out) throws Exception {
        byte[] logifles = msg.getLogfile().getBytes(CharsetUtil.UTF_8);
        byte[] message = msg.getMsg().getBytes(CharsetUtil.UTF_8);
        ByteBuf buffer = ctx.alloc().buffer(logifles.length + message.length + 1);
//        write the  filename  to the datagram
        buffer.writeBytes(logifles);
        //添加一个分隔符
        buffer.writeByte(LogEvent.SEPARATOR);
//        把日志时间内容写到buffer
        buffer.writeBytes(message);
        log.info("编码消息={}", msg.getMsg());
        DatagramPacket datagramPacket = new DatagramPacket(buffer, remoteAddress);
        out.add(datagramPacket);
    }

}
