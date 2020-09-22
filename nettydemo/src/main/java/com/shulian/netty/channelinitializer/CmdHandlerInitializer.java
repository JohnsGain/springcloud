package com.shulian.netty.channelinitializer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import lombok.Getter;

/**
 * These decoders are tools for implementing your own delimited protocols. As an
 * example, we’ll use the following protocol specification:
 * ■ The incoming data stream is a series of frames, each delimited by a line feed (\n).
 * ■ Each frame consists of a series of items, each delimited by a single space character.
 * ■ The contents of a frame represent a command, defined as a name followed by a
 * variable number of arguments.
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2020-09-22 15:16
 * @since jdk1.8
 */
public class CmdHandlerInitializer extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch) throws Exception {

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



}
