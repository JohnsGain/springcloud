package com.shulian.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * As you can see from the frames on the right side of the figure, this particular decoder
 * produces frames with a fixed size of 3 bytes. Thus it may require more than one event
 * to provide enough bytes to produce a frame.
 * Finally, each frame will be passed to the next ChannelHandler in the ChannelPipeline.
 * <p>
 * <p>
 * 实现一个特殊的解码器，需要把传入的数据封装成固定长度的帧，3个字节一个帧发送到下一个处理器。如果当前事件接收的数据
 * <p>
 * 不够3个字节，就会等待下一次事件接收数据
 *
 * ByteToMessageDecoder 属于 ChannelInboundHandler 实现类，专门对读取的数据进行解码处理
 * @author zhangjuwa
 * @apiNote
 * @date 2020-09-16 16:21
 * @since jdk1.8
 */
public class FixedLengthFrameDecoder extends ByteToMessageDecoder {

    private final int frameLength;

    public FixedLengthFrameDecoder(int frameLength) {
        if (frameLength <= 0) {
            throw new IllegalArgumentException(
                    "frameLength must be a positive integer: " + frameLength);
        }
        this.frameLength = frameLength;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
//        Checks if enough bytes can be read to produce the next frame
        while (in.readableBytes() >= frameLength) {
//            reads a new frame out of the bytebuf
            ByteBuf bytes = in.readBytes(frameLength);
//            add the new frame to the decoded list
            out.add(bytes);
        }
    }


}
