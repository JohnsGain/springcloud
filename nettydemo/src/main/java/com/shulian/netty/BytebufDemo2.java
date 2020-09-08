package com.shulian.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.charset.Charset;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2020-04-05 20:31
 * @since jdk1.8
 */
@Slf4j
public class BytebufDemo2 {

    /**
     * getset操作，指针不会移动
     * getMedium:从参数所在下标参数开始，选择三个字节，合成一个24位bit，然后转换成10进制返回。第一个字节是高16-24位，第二个是8-16，
     * 第三个是0-8；
     * getInt:同理，返回连着4个字节组成的32位bit的十进制
     * getlong:8个字节。
     * getShort:2个字节
     * getBoolean:如果下标处字节不等于0，就是true
     */
    @Test
    public void getSet() {
        ByteBuf buffer = Unpooled.wrappedBuffer("不0会jpan".getBytes(CharsetUtil.UTF_8));
        System.out.println(buffer.capacity() + ";" + buffer.getBoolean(1) + " " + buffer.getUnsignedMedium(3) +
                " " + buffer.getLong(3) + " " + buffer.getInt(3) + " " + buffer.getByte(3));
        System.out.println(buffer.getMedium(3));

//        Integer.valueOf("0xff",)
        String string = Integer.toBinaryString(Integer.parseInt("ff", 16));
        System.out.println(string);
    }

    /**
     * 读写操作，指针会移动
     */
    @Test
    public void readWrite() {

        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf byteBuf = Unpooled.buffer(5, 100);

//        byteBuf.hasArray()
        byteBuf.writeByte(5);
        log.info("readInex={},wirteInde={},capa={}", byteBuf.readerIndex(), byteBuf.writerIndex(), byteBuf.capacity());

        byteBuf.writeInt(55);
        log.info("readInex={},wirteInde={},capa={}", byteBuf.readerIndex(), byteBuf.writerIndex(), byteBuf.capacity());

        byteBuf.writeDouble(.1);
        log.info("readInex={},wirteInde={},capa={}", byteBuf.readerIndex(), byteBuf.writerIndex(), byteBuf.capacity());

        byte aByte = byteBuf.readByte();
        System.out.println(aByte);
        log.info("readInex={},wirteInde={},capa={}", byteBuf.readerIndex(), byteBuf.writerIndex(), byteBuf.capacity());

        System.out.println(byteBuf.readInt());
        log.info("readInex={},wirteInde={},capa={}", byteBuf.readerIndex(), byteBuf.writerIndex(), byteBuf.capacity());
        System.out.println(byteBuf.readDouble());
        log.info("readInex={},wirteInde={},capa={}", byteBuf.readerIndex(), byteBuf.writerIndex(), byteBuf.capacity());

        System.out.println(byteBuf.maxCapacity());
    }

    /**
     * ByteBufUtil provides static helper methods for manipulating a ByteBuf. Because this
     * API is generic and unrelated to pooling, these methods have been implemented outside the allocation classes.
     *
     * 其中两个重要的api： hexdump:按16进制格式打印bytebuf的字节内容
     * equal(bytebuf1,bytebuf2)
     */
    @Test
    public void bytebufUtil() {
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello world!", CharsetUtil.UTF_8);
        //以16进制格式打印bytebuf内容
        String hexDump = ByteBufUtil.hexDump(byteBuf);
        System.out.println(hexDump);
        System.out.println("Integer.toBinaryString(-17)=="+Integer.toBinaryString(-17));
        int count = Integer.bitCount(17);
        System.out.println(count);
//        Integer.parseInt(hexDump,hexDump)
        ByteBuf wrappedBuffer = Unpooled.wrappedBuffer("hello world!".getBytes(CharsetUtil.UTF_8));


    }

    /**
     * 都转换成10进制
     */
    @Test
    public void radix() {
        System.out.println(Integer.parseInt("FF", 16));
        System.out.println(Integer.parseInt("05060708", 16));
        System.out.println(Integer.parseInt("77", 8));
        Integer a = 0x05060708;
        byte value = a.byteValue();

    }


}
