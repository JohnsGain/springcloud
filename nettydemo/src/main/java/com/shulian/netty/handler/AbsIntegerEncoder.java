package com.shulian.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * 按4字节读出int值，然后获取这个Int值的绝对值
 * @author zhangjuwa
 * @apiNote
 * @date 2020-09-16 22:38
 * @since jdk1.8
 */
public class AbsIntegerEncoder extends MessageToMessageEncoder<ByteBuf> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        while (msg.readableBytes() >= 4) {
            int readInt = msg.readInt();
            out.add(Math.abs(readInt));
        }
    }
}
