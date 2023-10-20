package com.shulian.netty.udp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * The first decoder in the pipeline, LogEventDecoder, is responsible for decoding
 * incoming DatagramPackets to LogEvent messages (a typical setup for any Netty application
 * that transforms inbound data). The following listing shows the implementation
 * @author zhangjuwa
 * @apiNote
 * @date 2020-10-03 17:22
 * @since jdk1.8
 */
public class LogEventDecoder extends MessageToMessageDecoder<DatagramPacket> {

    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket packet, List<Object> out) throws Exception {
        ByteBuf content = packet.content();
        int index = content.indexOf(0, content.readableBytes(), LogEvent.SEPARATOR);
        String filename = content.slice(0, index).toString(CharsetUtil.UTF_8);
        String message = content.slice(index + 1, content.readableBytes()).toString(CharsetUtil.UTF_8);
        LogEvent logEvent = new LogEvent(packet.sender(), System.currentTimeMillis(), filename, message);
        out.add(logEvent);
    }
}
