package com.shulian.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Let’s imagine we have a stream of bytes written from a remote peer to us, and that it contains
 * simple integers. We want to handle each integer separately later in the ChannelPipeline, so
 * we want to read the integers from the inbound ByteBuf and pass each integer separately to
 * the next ChannelInboundHandler in the ChannelPipeline. As we need to “decode” bytes into
 * integers we will extend the ByteToMessageDecoder in our implementation, which we call
 * ToIntegerDecoder.
 *
 *
 <p>
 * Be aware that sub-classes of {@link ByteToMessageDecoder} <strong>MUST NOT</strong>
 * annotated with {@link @Sharable}.
 * <p>
 * @author zhangjuwa
 * @apiNote
 * @date 2020-04-07 12:21
 * @since jdk1.8
 */
@Slf4j
public class ToIntegerDecoder extends ByteToMessageDecoder {

    /**
     * #1 Implementation extends ByteToMessageDecode to decode bytes to messages
     * #2 Check if there are at least 4 bytes readable (and int is 4 bytes long)
     * #3 Read integer from inbound ByteBuf, add to the List of decodec messages
     *
     * @param ctx
     * @param in
     * @param out
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() > 4) {
            out.add(in.readInt());
        }
    }
}
