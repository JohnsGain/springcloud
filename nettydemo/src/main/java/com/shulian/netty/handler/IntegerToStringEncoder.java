package com.shulian.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * there are more complex examples which use the MessageToMessageEncoder. One of
 * those is the ProtobufEncoder which can be found in the io.netty.handler.codec.protobuf
 * package.
 * @author zhangjuwa
 * @apiNote
 * @date 2020-04-07 16:42
 * @since jdk1.8
 */
@Slf4j
public class IntegerToStringEncoder extends MessageToMessageEncoder<Integer> {

    /**
     * The encoder encodes Integer messages and forward the Integers to the next
     * ChannelOutboundHandler in the ChannelPipeline.
     * @param ctx x
     * @param msg x
     * @param out x
     * @throws Exception x
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Integer msg, List<Object> out) throws Exception {
        out.add(String.valueOf(msg));
    }

}
