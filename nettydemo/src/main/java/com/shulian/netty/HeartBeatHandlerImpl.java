package com.shulian.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Lee HN
 * @date 2019/6/30 1:36
 */
@Slf4j
public class HeartBeatHandlerImpl extends ChannelInboundHandlerAdapter {

    private TcpChannelGroup channelGroup;

    private int readIdleTimes = 0;

    public static final String ping = "ping";

    public static final String pong = "pong";



    public HeartBeatHandlerImpl(TcpChannelGroup channelGroup) {
        this.channelGroup = channelGroup;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        readIdleTimes = 0;
        String message = (String) msg;
        log.debug("======> [server]收到心跳:" + message);
        if (StringUtils.equals(ping, message)) {
            ctx.writeAndFlush(pong);
            log.debug("======> [server]返回心跳:" + pong);
        } else super.channelRead(ctx, msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("=== " + ctx.channel().remoteAddress() + " is active ===");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            //String eventType;
            switch (event.state()) {
                case READER_IDLE:
                    //eventType = "读空闲";
                    readIdleTimes++; // 读空闲的计数加1
                    break;
                case WRITER_IDLE:
                    //eventType = "写空闲";
                    break;
                case ALL_IDLE:
                    //eventType = "读写空闲";
                    break;
            }
            //log.debug(StringUtils.join(username, " 超时事件:", eventType));
            if (readIdleTimes > 2) {
                String username = ctx.channel().attr(AttributeKey.<String>valueOf("username")).get();
                log.warn((username == null ? "匿名" : username) + " 读空闲达到3次，关闭连接");
                ctx.writeAndFlush("close");
                ctx.close();
                channelGroup.remove(ctx.channel());
            }
        } else super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage());
    }
}
