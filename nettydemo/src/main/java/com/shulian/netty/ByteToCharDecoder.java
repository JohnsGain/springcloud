package com.shulian.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2020-04-07 17:56
 * @since jdk1.8
 */
@Slf4j
public class ByteToCharDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        while (in.readableBytes() > 2) {
            out.add(in.readChar());
        }
    }
}
