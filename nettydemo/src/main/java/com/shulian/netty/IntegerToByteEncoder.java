package com.shulian.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * As a counterpart to the decoders Netty offers, base classes help you to write encoders in an
 * easy way. Again, these are divided into different types similar to what you saw in section 7.2:
 * • Encoders that encode from message to bytes
 * • Encoders that encode from message to message
 * @author zhangjuwa
 * @apiNote
 * @date 2020-04-07 16:19
 * @since jdk1.8
 */
@Slf4j
public class IntegerToByteEncoder extends MessageToByteEncoder<Integer> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Integer msg, ByteBuf out) throws Exception {
        out.writeInt(msg);
    }

    //    为什么ByteToMessageDecoder需要实现两个抽象类，而MessageToByteEncoder不需要？ 、
  /*  The reason why there is only one method compared to decoders where we have two messages
    is that decoders often need to to «produce» a last message once the Channel was closed. For
this reason the decodeLast() method is used. This is not the case for an encoder where there
    is no need to produce any extra message when the connection was closed as there would be
    nothing you could do with the produced message, as writing to a closed Channel would fail
    anyway.*/
}
