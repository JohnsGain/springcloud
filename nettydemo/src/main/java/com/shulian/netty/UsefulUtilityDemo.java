package com.shulian.netty;

import io.netty.buffer.ByteBufUtil;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.PlatformDependent;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

/**
 * 一些有用的工具，如ByteBufAllocator,ByteBufUtil,Unpooled
 * <p>
 * As mentioned before, Netty supports pooling for the various ByteBuf implementations, this
 * greatly eliminate the overhead of allocating and deallocating memory. To make this possible it
 * provides an abstraction called ByteBufAllocator. As the name implies it's responsible for
 * allocating ByteBuf instances of the previously explained types. Whether these are pooled or
 * not is specific to the implementation but doesn’t change the way you operate on it.
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2020-04-05 22:12
 * @since jdk1.8
 */
@Slf4j
public class UsefulUtilityDemo {

//    buffer()
//    buffer(int);
//    buffer(int, int);
//    Return a ByteBuf that may be of type heap or direct depend
//    on the implementation.
//            heapBuffer()

//    heapBuffer(int)
//    heapBuffer(int, int)
//    Return a ByteBuf of type heap.

//    directBuffer()
//    directBuffer(int)
//    directBuffer(int, int)
//    Return a ByteBuf of type direct.

//    compositeBuffer()
//    compositeBuffer(int);
//    heapCompositeBuffer ()
//    heapCompositeBuffer(int);
//    directCompositeBuffer ()
//    directCompositeBuffer(int);
//    Return a CompositeByteBuf that may expand internally if
//    needed using a heap or direct buffer.

//            ioBuffer()
//    Return a ByteBuf that will be used for I/O operations which is
//    reading from the socket.

    /**
     * 缓冲区分配器使用了内存分配算法jemalloc,这种算法也在很多操作系统层面被使用来高效内存分配
     */
    @Test
    public void alloc() {
//        Channel channel = ...;
//        ByteBufAllocator allocator = channel.alloc(); #1
//....
//        ChannelHandlerContext ctx = ...;
//        ByteBufAllocator allocator2 = ctx.alloc();
        System.out.println(PlatformDependent.isOsx());
        System.out.println(PlatformDependent.isWindows());
    }

    @Test
    public void ByteBufUtil() {
        //把缓冲区数据按照16进制格式打印
        String dump = ByteBufUtil.hexDump("滚滚水电费".getBytes(CharsetUtil.UTF_8));
        System.out.println(dump);

        System.out.println(ByteBufUtil.hexDump(new byte[]{1, 12, 34, 53}));

        //比较两个buffer是否相等
        //        ByteBufUtil.equals()

        //#Try to release the buffer which will decrement the reference count by 1. Once the reference count
//        reaches 0 it is released and true is returned.
//        ByteBuf buffer = ...;
//        boolean released = buffer.release(); #1
//...

    }

}
