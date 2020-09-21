package com.shulian.netty.handler;

import io.netty.channel.CombinedChannelDuplexHandler;

/**
 * Now that we have a decoder and encoder, we’ll combine them to build up a codec.
 * This listing shows how this is done.
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2020-09-18 15:52
 * @since jdk1.8
 */
public class CombinedByteCharCodec extends CombinedChannelDuplexHandler<ByteToCharDecoder, CharToByteEncoder> {

    public CombinedByteCharCodec() {
        super(new ByteToCharDecoder(), new CharToByteEncoder());
    }
}
