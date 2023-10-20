package com.shulian.netty.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;


/**
 * @author Lee HN
 * @date 2019/6/30 1:14
 */
public class ChannelInit extends ChannelInitializer<SocketChannel> {

    private StringDecoder stringDecoder = new StringDecoder();

    private StringEncoder stringEncoder = new StringEncoder();

    DelimiterEncoder delimiterEncoder = new DelimiterEncoder(new CommonProperties());

    private TcpChannelGroup channelGroup = new TcpChannelGroup(new ObjectMapper());

    AuthenticateHandler authenticateHandler = new AuthenticateHandler();
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(
                new DelimiterBasedFrameDecoder(1024, delimiterEncoder.getDelimiterBuf()),
                stringDecoder, stringEncoder, delimiterEncoder, authenticateHandler,
                new IdleStateHandler(11, 0, 0),
                new HeartBeatHandlerImpl(channelGroup));
    }
}
