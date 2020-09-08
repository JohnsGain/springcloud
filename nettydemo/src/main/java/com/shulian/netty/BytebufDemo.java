package com.shulian.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufProcessor;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ByteProcessor;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 演示netty的零拷贝
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2020-03-31 15:10
 * @since jdk1.8
 */
@Slf4j
public class BytebufDemo {

    /**
     * Netty 提供了 CompositeByteBuf 类, 它可以将多个 ByteBuf 合并为一个逻辑上的 ByteBuf, 避免了各个 ByteBuf 之间的拷贝
     */
    @Test
    public void composite() {
        //我们将 header 和 body 都拷贝到了新的 allBuf 中了, 这无形中增加了两次额外的数据拷贝操作了
        ByteBuf header = Unpooled.wrappedBuffer("header".getBytes(CharsetUtil.UTF_8));
        ByteBuf body = Unpooled.wrappedBuffer("body".getBytes(CharsetUtil.UTF_8));
        ByteBuf allCont = Unpooled.buffer(header.readableBytes() + body.readableBytes());
        allCont.writeBytes(header);
        allCont.writeBytes(body);

        //我们来看一下 CompositeByteBuf 是如何实现这样的需求的吧,方法将 header 与 body 合并为一个逻辑上的 ByteBuf,
//        其中第一个参数是 true, 表示当添加新的 ByteBuf 时, 自动递增 CompositeByteBuf 的 writeIndex.
        CompositeByteBuf compositeBuffer = Unpooled.compositeBuffer();
        compositeBuffer.addComponents(true, header, body);

//        除了上面直接使用 CompositeByteBuf 类外, 我们还可以使用 Unpooled.wrappedBuffer 方法,
//        它底层封装了 CompositeByteBuf 操作, 因此使用起来更加方便
        ByteBuf wrappedBuffer = Unpooled.wrappedBuffer(header, body);
    }

    /**
     * heap buffer:如果要把数据传输到套接字，会有一次从应用程序缓冲区到内核缓冲区的数据拷贝，
     * 内部使用的是字节数组存储。可以直接获取到字节数组进行数据处理
     */
    @Test
    public void heap() {
        //创建一个Headbuffer
        ByteBuf heap = Unpooled.wrappedBuffer("xxxxdata".getBytes(CharsetUtil.UTF_8));
        if (heap.hasArray()) {
            byte[] array = heap.array();
            int offset = heap.arrayOffset();
            int bytes = heap.readableBytes();
            doSomething(array, offset, bytes);
        }

    }

    /**
     * direct buffer:堆外直接内存。当用于把数据传输到套接字的时候，使用这种buffer可以减少一次不必要的数据拷贝。
     * 通过监控工具查看堆内存占用的时候将不会统计这一块内存的占用，但是做应用程序内存占用统计的时候应该要算上这一部分占用。
     * direct buffer分配和释放内存比较消耗性能力，所以netty用了池化的技术，解决这个问题；如果需要direct里面的数据
     * 在遗留代码里面进行业务处理，就需要一次从内核缓冲区到应用程序缓冲区的复制，和heap方式不同， 他不能直接通过字节数组访问数据。
     */
    @Test
    public void direct() {
        ByteBuf directBuffer = Unpooled.directBuffer(1024, 1024 * 1024);
        if (!directBuffer.hasArray()) {
            directBuffer.writeDouble(8.9);
            int readableBytes = directBuffer.readableBytes();
            byte[] data = new byte[readableBytes];
            //这种 getBytes方式和readBytes不同，他不会改变buffer的读写下标
            directBuffer.getBytes(5, data);
            doSomething(data, 0, readableBytes);

        }
    }

    /**
     * Be aware that index pased access will not advance the readerIndex or writerIndex. You can
     * advance it by hand by call readerIndex(index) and writerIndex(index) if needed.
     */
    @Test
    public void getOrRead() {
        //创建一个Headbuffer
        ByteBuf heap = Unpooled.wrappedBuffer("0213data".getBytes(CharsetUtil.UTF_8));
        for (int i = 0; i < heap.capacity(); i++) {
            byte date = heap.getByte(i);
            log.info("数据={}", (date));
        }

        // read
        while (heap.isReadable()) {
            byte aByte = heap.readByte();
            log.info("数据={},readIndex={}", (aByte), heap.readerIndex());
        }
        //已经读完，会报错 IndexOutOfBoundsException
//        heap.readByte();

    }

    @Test
    public void write() {
        ByteBuf byteBuf = Unpooled.buffer(5, 100);
        log.info("readInex={},wirteInde={},capa={}", byteBuf.readerIndex(), byteBuf.writerIndex(), byteBuf.capacity());
        ThreadLocalRandom localRandom = ThreadLocalRandom.current();
        while (byteBuf.isWritable()) {
            byteBuf.writeByte(localRandom.nextInt());
        }
        log.info("readInex={},wirteInde={},byteBuf.isWritable={}", byteBuf.readerIndex(), byteBuf.writerIndex(), byteBuf.isWritable());
        byteBuf.writeByte(123);
        log.info("readInex={},wirteInde={},byteBuf.isWritable={}", byteBuf.readerIndex(), byteBuf.writerIndex(), byteBuf.isWritable());
        while (byteBuf.isWritable()) {
            byteBuf.writeByte(localRandom.nextInt());
        }
        log.info("readInex={},wirteInde={},byteBuf.isWritable={}", byteBuf.readerIndex(), byteBuf.writerIndex(), byteBuf.isWritable());

        byteBuf.writeBytes(Unpooled.wrappedBuffer("不会".getBytes(CharsetUtil.UTF_8)));
        log.info("readInex={},wirteInde={},byteBuf.isWritable={}", byteBuf.readerIndex(), byteBuf.writerIndex(), byteBuf.isWritable());

    }

    /**
     * 把已经读取的buffer空间压缩了，腾出更多空间用于写数据进来。 readIndex重置为0，这个操作官方文档不推荐频繁调用，因为会引起数据的
     * 内存拷贝，只有在需要尽快释放内存的时候使用。
     */
    @Test
    public void discardReadBytes() {
        ByteBuf byteBuf = Unpooled.wrappedBuffer(new byte[]{12, 34, 45, 66, 23, 12, 123});
        log.info("readInex={},wirteInde={},capa={}", byteBuf.readerIndex(), byteBuf.writerIndex(), byteBuf.capacity());
        ByteBuf byteBuf1 = byteBuf.readBytes(2);
        log.info("readInex={},wirteInde={},capa={}", byteBuf.readerIndex(), byteBuf.writerIndex(), byteBuf.capacity());
//        调用 discardReadBytes，readIndex重置为0，已读取的数据丢弃。
        byteBuf.discardReadBytes();
        log.info("readInex={},wirteInde={},capa={}", byteBuf.readerIndex(), byteBuf.writerIndex(), byteBuf.capacity());

    }

    /**
     * You can set both readerIndex and writerIndex to 0 by calling clear(). It doesn’t clear the
     * buffer’s content (for example, filling with 0) but clears the two pointers. Please note that the
     * semantics of this operation are different from the JDK’s ByteBuffer.clear().
     * <p>
     * Compared to discardReadBytes(), the clear() operation is cheap, because it just adjust
     * the indexes (readerIndex and writerIndex) and doesn’t need to copy any memory.
     */
    @Test
    public void clear() {
        ByteBuf byteBuf = Unpooled.wrappedBuffer(new byte[]{12, 34, 45, 66, 23, 12, 123});
        log.info("readInex={},wirteInde={},capa={}", byteBuf.readerIndex(), byteBuf.writerIndex(), byteBuf.capacity());
        ByteBuf byteBuf1 = byteBuf.readBytes(2);
        log.info("readInex={},wirteInde={},capa={}", byteBuf.readerIndex(), byteBuf.writerIndex(), byteBuf.capacity());
//        调用 clear，readIndex和writeindex重置为0。
        byteBuf.clear();
        log.info("readInex={},wirteInde={},capa={}", byteBuf.readerIndex(), byteBuf.writerIndex(), byteBuf.capacity());
    }

    /**
     * Various indexOf() methods help you locate an index of a value which meets a certain criteria.
     * <p>
     * Using the bytesBefore()
     * method, you can easily consume data from Flash without manually readying every byte in the
     * data to check for NULL bytes.
     */
    @Test
    public void indexOf() {
        ByteBuf byteBuf = Unpooled.wrappedBuffer(new byte[]{12, 23,34, 0,13,32, 66, 23, 12, 123});
        int index = byteBuf.indexOf(0, byteBuf.capacity(), (byte) 66);
        System.out.println(index);
        //返回23的下标，从当前readIndex开始查找
        ByteBuf byteBuf1 = byteBuf.readBytes(2);
        log.info("readInex={},wirteInde={},capa={}", byteBuf.readerIndex(), byteBuf.writerIndex(), byteBuf.capacity());
        int before = byteBuf.bytesBefore((byte) 23);
        System.out.println(before);

        // null-terminated string 就是以指以'\0'结尾的字符串，相对于Char类型来说的
        System.out.println((int)'\0');
        int nul = byteBuf.forEachByte(ByteProcessor.FIND_NUL);
        System.out.println("nul=="+nul);

        // \r 换行符的asc码是13
        System.out.println((int)'\r');
        // 从当前readIndex开始查找，找出第一个 换行符 下标
        System.out.println(byteBuf.forEachByte(ByteProcessor.FIND_CR));

    }

    @Test
    public void markOrResset() {
        ByteBuf byteBuf = Unpooled.wrappedBuffer(new byte[]{12, 34, 45, 66, 23, 12, 123});
        log.info("readInex={},wirteInde={},capa={}", byteBuf.readerIndex(), byteBuf.writerIndex(), byteBuf.capacity());
        byteBuf.readByte();
        log.info("readInex={},wirteInde={},capa={}", byteBuf.readerIndex(), byteBuf.writerIndex(), byteBuf.capacity());
        byteBuf.readChar();
        byteBuf.markReaderIndex();
        byteBuf.readByte();
        byteBuf.resetReaderIndex();
        log.info("readInex={},wirteInde={},capa={}", byteBuf.readerIndex(), byteBuf.writerIndex(), byteBuf.capacity());

        //这里可能报错，当索引重置之后，写索引小于读索引的时候，抛出 IndexOutOfBoundsException
        byteBuf.resetWriterIndex();
        log.info("readInex={},wirteInde={},capa={}", byteBuf.readerIndex(), byteBuf.writerIndex(), byteBuf.capacity());

    }

    /**
     * 视图副本创建方式
     * To create a view of an existing buffer, call duplicate(), slice(), slice(int, int),
     * readOnly(), or order(ByteOrder). A derived buffer has an independent readerIndex,
     * writerIndex, and marker indexes, but it shares other internal data representation the way a
     * NIO ByteBuffer does.
     */
    @Test
    public void viewCopy() {
//        视图副本创建方式
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!ì", utf8);
        ByteBuf sliced = buf.slice(0, 14);
        System.out.println(sliced.toString(utf8));
        buf.setByte(0, (byte) 'J');
        assert buf.getByte(0) == sliced.getByte(0);
        System.out.println(sliced.toString(utf8));

//        copy创建的是一个完整的数据副本
        ByteBuf copiedBuffer = Unpooled.copiedBuffer("Netty in Action rocks!ì", utf8);
        ByteBuf copy = buf.copy(0, 14);
        System.out.println(copy.toString(utf8));
        buf.setByte(0, (byte) 'J');
        assert copiedBuffer.getByte(0) != copy.getByte(0);
        System.out.println(copiedBuffer.toString(utf8));
        System.out.println(copy.toString(utf8));
    }

    private void doSomething(byte[] array, int offset, int bytes) {

    }

}
