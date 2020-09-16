package com.shulian.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

/**
 * the maximum frame size has been set to 3 bytes. If the size of a
 * frame exceeds that limit, its bytes are discarded and a TooLongFrameException is
 * thrown. The other ChannelHandlers in the pipeline can either handle the exception
 * in exceptionCaught() or ignore it.
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2020-09-16 23:14
 * @since jdk1.8
 */
public class FrameChunkDecoder extends ByteToMessageDecoder {

    private final int maxFrameSize;

    public FrameChunkDecoder(int maxFrameSize) {
        this.maxFrameSize = maxFrameSize;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int readableBytes = in.readableBytes();
        if (readableBytes > maxFrameSize) {
            in.clear();
            throw new TooLongFrameException();
        }
        ByteBuf byteBuf = in.readBytes(readableBytes);
        out.add(byteBuf);
    }
}
