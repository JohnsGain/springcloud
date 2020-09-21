package com.shulian.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

/**
 * you’re able to decode them. Consequently, you mustn’t allow your decoder to buffer
 * enough data to exhaust available memory. To address this common concern, Netty
 * provides a TooLongFrameException, which is intended to be thrown by decoders if a
 * frame exceeds a specified size limit.
 * To avoid this you can set a threshold of a maximum number of bytes which, if
 * exceeded, will cause a TooLongFrameException to be thrown (and caught by ChannelHandler.exceptionCaught()). It will then be up to the user of the decoder to decide
 * how to handle the exception. Some protocols, such as HTTP, may allow you to return a
 * special response. In other cases, the only option may be to close the connection.
 * <p>
 * <p>
 * a ByteToMessageDecoder can make use of TooLongFrameException to notify other ChannelHandlers in the
 * ChannelPipeline about the occurrence of a frame-size overrun. Note that this kind of protection
 * is especially important if you are working with a protocol that has a variable frame size.
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2020-09-18 14:48
 * @since jdk1.8
 */
public class SafeByteToMessageDecoder extends ByteToMessageDecoder {

    private static final int MAX_FRAME_SIZE = 1024;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int readableBytes = in.readableBytes();
        if (readableBytes >= MAX_FRAME_SIZE) {
            in.skipBytes(MAX_FRAME_SIZE);
            throw new TooLongFrameException("Frame too big!");
        }
        //DO STH
    }

}
