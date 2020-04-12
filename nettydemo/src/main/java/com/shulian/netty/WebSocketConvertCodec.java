package com.shulian.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Sometimes you need to write a codec that’s used to convert from one message to another
 * message type and vice versa. MessageToMessageCodec makes this a piece of cake.
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2020-04-07 17:06
 * @since jdk1.8
 */
@Slf4j
public class WebSocketConvertCodec extends MessageToMessageCodec<WebSocketFrame, WebSocketConvertCodec.MyWebSocketFrame> {

    /**
     * 从 MyWebSocketFrame e加密成 WebSocketFrame
     *
     * @param ctx
     * @param msg
     * @param out
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, MyWebSocketFrame msg, List<Object> out) throws Exception {
        ByteBuf payload = msg.getData().duplicate().retain();
        switch (msg.getType()) {
            case BINARY:
                out.add(new BinaryWebSocketFrame(payload));
                return;
            case CLOSE:
                out.add(new CloseWebSocketFrame(true, 0, payload));
                return;
            case PING:
                out.add(new PingWebSocketFrame(payload));
                return;
            case PONG:
                out.add(new PongWebSocketFrame(payload));
                return;
            case TEXT:
                out.add(new TextWebSocketFrame(payload));
                return;
            case CONTINUATION:
                out.add(new ContinuationWebSocketFrame(payload));
                return;
            default:
                throw new IllegalStateException("Unsupported websocket msg " + msg);
        }
    }

    /**
     * 从 WebSocketFrame 解密成 MyWebSocketFrame
     *
     * @param ctx
     * @param msg
     * @param out
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) throws Exception {
        ByteBuf payload = msg.content().duplicate().retain();
        if (msg instanceof BinaryWebSocketFrame) {
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.BINARY, payload));
            return;
        }
        if (msg instanceof CloseWebSocketFrame) {
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.CLOSE, payload));
            return;
        }
        if (msg instanceof PingWebSocketFrame) {
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.PING, payload));
            return;
        }
        if (msg instanceof PongWebSocketFrame) {
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.PONG, payload));
            return;
        }
        if (msg instanceof TextWebSocketFrame) {
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.TEXT, payload));
            return;
        }
        if (msg instanceof ContinuationWebSocketFrame) {
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.CONTINUATION, payload));
            return;
        }
        throw new IllegalStateException("Unsupported websocket msg " + msg);
    }

    public static final class MyWebSocketFrame {
        public enum FrameType {
            BINARY,
            CLOSE,
            PING,
            PONG,
            TEXT,
            CONTINUATION
        }

        private final FrameType type;
        private final ByteBuf data;

        public MyWebSocketFrame(FrameType type, ByteBuf data) {
            this.type = type;
            this.data = data;
        }

        public FrameType getType() {
            return type;
        }

        public ByteBuf getData() {
            return data;
        }
    }
}
