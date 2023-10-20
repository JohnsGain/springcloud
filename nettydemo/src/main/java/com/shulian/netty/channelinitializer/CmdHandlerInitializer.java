package com.shulian.netty.channelinitializer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.LineBasedFrameDecoder;
import lombok.Getter;

/**
 * These decoders are tools for implementing your own delimited protocols. As an
 * example, we’ll use the following protocol specification:
 * ■ The incoming data stream is a series of frames, each delimited by a line feed (\n).
 * 输入数据是一些列帧，以换行符分割
 * ■ Each frame consists of a series of items, each delimited by a single space character.
 * 每个帧包含一系列数据项(命令的名称和参数)，每个数据项之间用 单个空格符 分割
 * ■ The contents of a frame represent a command, defined as a name followed by a
 * variable number of arguments.
 * 一个帧是一个命令，由一个名称和若干个参数组成。
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2020-09-22 15:16
 * @since jdk1.8
 */
public class CmdHandlerInitializer extends ChannelInitializer<Channel> {

    static final byte SPACE = (byte) ' ';

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new CmdDecoder(64 * 1024));
        pipeline.addLast(new CmdHandler());
    }

    /**
     * Stores the contents of the frame (a command) in one ByteBuf for the
     * name and another for the arguments
     */
    @Getter
    public static final class Cmd {
        private final ByteBuf name;
        private final ByteBuf args;

        public Cmd(ByteBuf name, ByteBuf args) {
            this.name = name;
            this.args = args;
        }
    }

    public static final class CmdDecoder extends LineBasedFrameDecoder {

        public CmdDecoder(int maxLength) {
            super(maxLength);
        }

        @Override
        protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
            ByteBuf frame = (ByteBuf) super.decode(ctx, buffer);
            if (frame == null) {
                return null;
            }
            int index = frame.indexOf(frame.readerIndex(), frame.writerIndex(), SPACE);
            return new Cmd(frame.slice(frame.readerIndex(), index),
                    frame.slice(index + 1, frame.writableBytes()));

        }
    }

    /**
     * Receives the decoded Cmd object from the CmdDecoder and performs some processing on it
     *
     * HELLO. WORLD
     */
    public static final class CmdHandler extends SimpleChannelInboundHandler<Cmd> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Cmd msg) throws Exception {
// Do something with the command
        }
    }


}
