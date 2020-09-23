package com.shulian.netty.handler;

import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This listing shows how you can transmit a file’s contents using zero-copy by creating
 * a DefaultFileRegion from a FileInputStream and writing it to a Channel.
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2020-09-22 17:37
 * @since jdk1.8
 */
@Slf4j
public class BigDataTransferZeroCopyHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = ctx.channel();
        Path path = Paths.get(".....xxx");
        File file = path.toFile();
        try (FileInputStream inputStream = new FileInputStream(file);) {
            FileRegion fileRegion = new DefaultFileRegion(inputStream.getChannel(), 0, file.length());
            channel.writeAndFlush(fileRegion)
                    .addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            if (!future.isSuccess()) {
                                Throwable cause = future.cause();
                                log.warn("文件传输异常={}", cause.getMessage(), cause);
                            }
                        }
                    });

        }

    }

}
