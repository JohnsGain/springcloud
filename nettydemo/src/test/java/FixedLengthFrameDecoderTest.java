import com.shulian.netty.handler.FixedLengthFrameDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.CharsetUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * Now let’s create a unit test to make sure this code works as expected. As we pointed
 * out earlier, even in simple code, unit tests help to prevent problems that might occur
 * if the code is refactored in the future and to diagnose them if they do.
 *
 * 使用 EmbeddedChannel 测试 inbound 消息
 * @author zhangjuwa
 * @apiNote
 * @date 2020-09-16 17:02
 * @since jdk1.8
 */
public class FixedLengthFrameDecoderTest {

    @Test
    public void testFramesDecoded() {
        ByteBuf buffer = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buffer.writeByte(i);
        }
        ByteBuf duplicate = buffer.duplicate();
//        create a EmbeddedChannel and adds a FixedLengthFrameDecoder to be test with a frame length of 3;
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));
//        write byte to EmbeddedChannel,
        boolean writeInbound = embeddedChannel.writeInbound(duplicate.retain());
        System.out.println(writeInbound);
        System.out.println(embeddedChannel.finish());


//        read the produced messages and verified that there are 3 frames with 3 bytes each.
        ByteBuf readInbound = embeddedChannel.readInbound();
        System.out.println(readInbound.toString(CharsetUtil.UTF_8));
        Assert.assertEquals(buffer.readSlice(3), readInbound);
        readInbound.release();

        readInbound = embeddedChannel.readInbound();
        System.out.println(readInbound.toString(CharsetUtil.UTF_8));
        Assert.assertEquals(readInbound, buffer.readSlice(3));
        readInbound.release();

        readInbound = embeddedChannel.readInbound();
        System.out.println(readInbound.toString(CharsetUtil.UTF_8));
        Assert.assertEquals(readInbound, buffer.readSlice(3));
        readInbound.release();

        Assert.assertNull(embeddedChannel.readInbound());
        buffer.release();

    }

    @Test
    public void testFramesDecoded2() {
        ByteBuf buffer = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buffer.writeByte(i);
        }
        ByteBuf duplicate = buffer.duplicate();
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));

        boolean inbound = embeddedChannel.writeInbound(duplicate.readBytes(2));
        //第一个 inbound 返回false, 因为第一次只写了2个字节到inbound,还不够生成一个frame,至少3个字节
        System.out.println(inbound);

        inbound = embeddedChannel.writeInbound(duplicate.readBytes(2));
        System.out.println(inbound);
        inbound = embeddedChannel.writeInbound(duplicate.readBytes(5));
        System.out.println(inbound);
        System.out.println(embeddedChannel.finish());

        ByteBuf readInbound = embeddedChannel.readInbound();
        Assert.assertEquals(buffer.readSlice(3), readInbound);
        readInbound.release();

        readInbound = embeddedChannel.readInbound();
        Assert.assertEquals(buffer.readSlice(3), readInbound);
        readInbound.release();

        readInbound = embeddedChannel.readInbound();
        Assert.assertEquals(buffer.readSlice(3), readInbound);
        readInbound.release();
    }


}
