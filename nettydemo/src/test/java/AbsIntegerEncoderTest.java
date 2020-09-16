import com.shulian.netty.handler.AbsIntegerEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

/**
 * 使用 EmbeddedChannel 测试 outbound消息
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2020-09-16 22:41
 * @since jdk1.8
 */
public class AbsIntegerEncoderTest {

    @Test
    public void test() {
        ByteBuf buffer = Unpooled.buffer();
        for (int i = 0; i < 10; i++) {
            buffer.writeInt(i * (-1));
        }
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new AbsIntegerEncoder());
        boolean writeOutbound = embeddedChannel.writeOutbound(buffer);
        System.out.println(writeOutbound);
        System.out.println(embeddedChannel.finish());

        // read the produced messages ant assert that they contains absolute values
        for (int i = 0; i < 10; i++) {
            int outbound = embeddedChannel.readOutbound();
            System.out.println(outbound);
        }

        System.out.println("消息：" + embeddedChannel.readOutbound());

    }

}
