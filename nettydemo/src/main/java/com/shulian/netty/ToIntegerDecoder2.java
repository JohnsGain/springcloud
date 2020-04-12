package com.shulian.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * implement ToIntegerDecoder again but now with ReplayingDecoder
 *
 * ReplayingDecoder is a special abstract base class for byte-to-message decoding that would
 * be to hard to implement if you had to check if there’s enough data in the ByteBuf all the time
 * before calling operations on it. It does this by wrapping the input ByteBuf with a special
 * implementation that checks if there’s enough data ready and if not, throws a special Signal
 * that it handles internally to detect it. Once such a signal is thrown, the decode loop stops.
 *
 * ReplayingDecoder有三个限制：
 *
 * • Not all operations on the ByteBuf are supported, and if you call an unsupported
 * operation, it will throw an UnreplayableOperationException.
 * • ByteBuf.readableBytes() won’t return what you expect most of the time.
 * • It is slightly slower then using ByteToMessageDecoder
 *
 * 首要规则：如果使用ByteToMessageDecoder不会带来复杂性，那么优先选择基于ByteToMessageDecoder来实现decoder
 * @author zhangjuwa
 * @apiNote
 * @date 2020-04-07 12:37
 * @since jdk1.8
 */
@Slf4j
public class ToIntegerDecoder2 extends ReplayingDecoder<Void> {

    /**
     * #1 Implementation extends ReplayingDecoder to decode bytes to messages
     * #2 Read integer from inbound ByteBuf and add it to the List of decoded messages
     * @param ctx
     * @param in
     * @param out
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        out.add(in.readInt());
    }
}
