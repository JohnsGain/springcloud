package com.shulian.netty.udp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * The job of the second ChannelHandler is to perform some processing on the LogEvent
 * messages created by the {@link LogEventDecoder}. In this case, it will simply write them to System.out. In a
 * real-world application you might aggregate them with events originating from a different log
 * file or post them to a database. This listing, which shows the LogEventHandler,
 * illustrates the basic steps to follow
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2020-10-03 17:56
 * @since jdk1.8
 */
@Slf4j
public class LogEventHandler extends SimpleChannelInboundHandler<LogEvent> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogEvent event) throws Exception {
        String builder = event.getReceived() +
                " [" +
                event.getSource().toString() +
                "] [" +
                event.getLogfile() +
                "] : " +
                event.getMsg();
        System.out.println(builder);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.warn("处理器接收到异常", cause);
        ctx.close();
    }
}
