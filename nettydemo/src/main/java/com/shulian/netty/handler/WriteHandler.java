package com.shulian.netty.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * As you saw in listing 6.6, you can acquire a reference to the enclosing ChannelPipeline by calling the pipeline()
 * method of a ChannelHandlerContext. This enables
 * runtime manipulation of the pipeline’s ChannelHandlers, which can be exploited to
 * implement sophisticated designs. For example, you could add a ChannelHandler to a
 * pipeline to support a dynamic protocol change.
 * Other advanced uses can be supported by caching a reference to a ChannelHandlerContext for later use,
 * which might take place outside any ChannelHandler
 * methods and could even originate from a different thread. This listing shows this pattern being used to trigger an event.
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2020-09-09 11:55
 * @since jdk1.8
 */
@ChannelHandler.Sharable
public class WriteHandler extends ChannelHandlerAdapter {

    private ChannelHandlerContext context;

    /**
     * Stores reference
     * to ChannelHandlerContext for later use
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.context = ctx;
    }

    /**
     * Sends message using  previously stored ChannelHandlerContext
     * @param msg
     */
    public void send(String msg) {
        context.writeAndFlush(msg);
    }

}
