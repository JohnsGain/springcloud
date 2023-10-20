package com.shulian.netty.handler;

import io.netty.channel.CombinedChannelDuplexHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * When you use codecs that act as a combination of a decoder and encoder, you lose the
 * flexibility to use the decoder or encoder individually. You’re forced to either have both or
 * neither.
 * You may wonder if there’s a way around this inflexibility problem that still allows you to
 * have the decoder and encoder as a logic unit in the ChannelPipeline.
 * Fortunately, there is a solution for this called CombinedChannelDuplexHandler. Although
 * this handler isn’t part of the codec API itself, it’s often used to build up a codec. .
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2020-04-07 18:00
 * @since jdk1.8
 */
@Slf4j
public class CharDuplexCodec extends CombinedChannelDuplexHandler<ByteToCharDecoder, CharToByteEncoder> {

    public CharDuplexCodec() {
        super(new ByteToCharDecoder(), new CharToByteEncoder());
    }


}
