package com.shulian.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * when the implementation extends the MessageToMessageDecoder to decode from
 * one message type to another, the Type parameter (generic) is used to specify the input
 * parameter. In our case, this is an integer type.
 * For a more complex example, please refer to the {@link HttpObjectAggregator} which can be
 * found in the io.netty.handler.codec.http package.
 * @author zhangjuwa
 * @apiNote
 * @date 2020-04-07 15:47
 * @since jdk1.8
 */
@Slf4j
public class IntegerToStringDecoder extends MessageToMessageDecoder<Integer> {

    @Override
    protected void decode(ChannelHandlerContext ctx, Integer msg, List<Object> out) throws Exception {
        out.add(String.valueOf(msg));
    }
}
