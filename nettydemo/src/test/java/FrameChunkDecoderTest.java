import com.shulian.netty.handler.FrameChunkDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.TooLongFrameException;
import org.junit.Assert;
import org.junit.Test;

/**
 * 使用 EmbeddedChannel 测试 exception handling 消息
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2020-09-16 23:18
 * @since jdk1.8
 */
public class FrameChunkDecoderTest {

    @Test
    public void test() {
        ByteBuf buffer = Unpooled.buffer();
        for (int i = 0; i < 10; i++) {
            buffer.writeByte(i);
        }
        ByteBuf duplicate = buffer.duplicate();
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new FrameChunkDecoder(3));
        //写2个字节进去，没超过帧极限大小，能正常通过 FrameChunkDecoder
        boolean writeInbound = embeddedChannel.writeInbound(duplicate.readBytes(2));
        System.out.println(writeInbound);

        //写4个字节进去，超过 帧极限大小，会抛出异常
        try {
            writeInbound = embeddedChannel.writeInbound(duplicate.readBytes(4));
            System.out.println(writeInbound);
        } catch (TooLongFrameException e) {
            e.printStackTrace();
        }

        writeInbound = embeddedChannel.writeInbound(duplicate.readBytes(3));
        System.out.println(writeInbound);
        System.out.println(embeddedChannel.finish());

        ByteBuf readInbound = embeddedChannel.readInbound();
        Assert.assertEquals(readInbound, buffer.readSlice(2));
        readInbound.release();

        readInbound = embeddedChannel.readInbound();
        Assert.assertEquals(readInbound, buffer.skipBytes(4).readSlice(3));
        readInbound.release();
        buffer.release();

    }


}
