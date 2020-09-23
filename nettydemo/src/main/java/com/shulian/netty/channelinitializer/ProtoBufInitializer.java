package com.shulian.netty.channelinitializer;

import com.google.protobuf.MessageLite;
import io.netty.channel.*;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;

/**
 *
 * The last of Netty’s solutions for serialization is a codec that utilizes Protocol Buffers,
 * a data interchange format developed by Google and now open source. The code can be
 * found at https://github.com/google/protobuf.
 *  Protocol Buffers encodes and decodes structured data in a way that’s compact and
 * efficient. It has bindings for many programming languages, making it a good fit for
 * cross-language projects.
 * @author zhangjuwa
 * @apiNote
 * @date 2020-09-23 00:10
 * @since jdk1.8
 */
public class ProtoBufInitializer extends ChannelInitializer<Channel> {

    private final MessageLite messageLite;

    public ProtoBufInitializer(MessageLite messageLite) {
        this.messageLite = messageLite;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        pipeline.addLast(new ProtobufEncoder());
        pipeline.addLast(new ProtobufDecoder(messageLite));
        pipeline.addLast(new ObjectHandler());
    }

    private static class ObjectHandler extends SimpleChannelInboundHandler<Object> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
// Do something with the object
        }
    }

}
