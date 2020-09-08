package com.shulian.netty;

import org.junit.Test;

/**
 * BytebufAllocator 实例可以通过channel,channelHandlerContext获取；
 *
 * @author zhangjuwa
 * There may be situations where you don’t have a reference to a ByteBufAllocator. For
 * this case, Netty provides a utility class called Unpooled, which provides static helper
 * <p>
 * The Unpooled class also makes ByteBuf available to non-networking projects that can
 * benefit from a high-performance extensible buffer API and that don’t require other
 * Netty components.
 * @apiNote
 * @date 2020-09-04 14:05
 * @since jdk1.8
 */
public class UnpooledDemo {

    /**
     * buffer()
     * buffer(int initialCapacity)
     * buffer(int initialCapacity, int maxCapacity)
     * Returns an unpooled ByteBuf
     * with heap-based storage
     * directBuffer()
     * directBuffer(int initialCapacity)
     * directBuffer(int initialCapacity, int
     * maxCapacity)
     * Returns an unpooled ByteBuf
     * with direct storage
     * wrappedBuffer() Returns a ByteBuf, which wraps
     * the given data.
     * copiedBuffer() Returns a ByteBuf, which copies
     * the given data
     */
    @Test
    public void nint() {
//        Unpooled.buffer()
//        Unpooled.directBuffer()
//        Unpooled.wrappedBuffer(new byte[]{12})
//        Unpooled.compositeBuffer()
//        Unpooled.copiedBuffer("dd", CharsetUtil.UTF_8)
    }

}
