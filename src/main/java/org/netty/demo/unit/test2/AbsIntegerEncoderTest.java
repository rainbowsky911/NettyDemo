package org.netty.demo.unit.test2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

/**
 * @Title: AbsIntegerEncoderTest
 * @Description:
 * @author: zdw
 * @date: 2022/2/24 11:10
 */
public class AbsIntegerEncoderTest {
    @Test
    public void testEncoded() {
        //出站写入
        ByteBuf buf = Unpooled.buffer();
        for (int i = 1; i < 10; i++) {
            buf.writeInt(i * -1);
        }
        //创建一个EmbeddedChanel，并安装一个要测试的AbsIntegerEncoder
        EmbeddedChannel channel = new EmbeddedChannel(new AbsIntegerEncoder());
        // channel.pipeline().addLast(new AbsIntegerEncoder());

        //写入ByteBuf，调用readOutbound()方法将会产生数据
        channel.writeOutbound(buf);
        channel.finish();

        channel.readOutbound();
        for (int i = 1; i < 10; i++) {
            channel.readOutbound();
        }
        System.out.println(channel.readOutbound() == null);
    }
}
