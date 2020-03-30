package com.shulian.netty.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;
import lombok.Data;

import java.util.List;

/**
 * @author Lee HN
 * @date 2019/6/19 10:57
 * 自动添加粘包分隔符
 */
@Data
@ChannelHandler.Sharable
public class DelimiterEncoder extends MessageToMessageEncoder<String> {

    private final String delimiter;

    private final ByteBuf delimiterBuf;

    public DelimiterEncoder(CommonProperties commonProperties) {
        commonProperties = new CommonProperties();
        NettyProperties nettyProperties = new NettyProperties();
        nettyProperties.setDelimiter("bbbb");
        nettyProperties.setFrameLength(2048);
        nettyProperties.setTcpPort(9888);
        nettyProperties.setStrategy("nio");
        commonProperties.setNetty(nettyProperties);
        this.delimiter = commonProperties.getNetty().getDelimiter();
        this.delimiterBuf = Unpooled.copiedBuffer(delimiter.getBytes(CharsetUtil.UTF_8));
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
        out.add(msg + delimiter);
    }
}
