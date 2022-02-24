package org.netty.demo.unit.test2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Netty 使用 EmbeddedChannel 进行单元测试
 * https://www.codetd.com/article/2897593
 * @date: 2022/2/24 10:50
 */
public class FixedLengthFrameDecoderTest {
    @Test
    public void testFrameDecoded() {
        //创建一个ByteBuf，并存储9字节
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();
        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));
        //将数据写入EmbeddedChannel
        System.out.println(channel.writeInbound(input.retain()));//true
        //标记Channel为已完成状态
        System.out.println(channel.finish());//true

        //读取所生成的消息，并且验证是否有3帧，其中每帧都为3字节
        ByteBuf read = channel.readInbound();
        System.out.println(buf.readSlice(3).equals(read));//true
        read.array();


        read = channel.readInbound();
        System.out.println(buf.readSlice(3).equals(read));//true
        read.release();

        read = channel.readInbound();
        System.out.println(buf.readSlice(3).equals(read));//true
        read.release();

        System.out.println(channel.readInbound() == null);//true
        buf.release();
    }

    @Test
    public void testFramesDescode2() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();
        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));
        //返回false，因为没有一个完整的可供读取的帧
        System.out.println(channel.writeInbound(input.retain()));//false
        //  System.out.println(channel.writeInbound(input.readBytes(7)));//true

        System.out.println(channel.finish());//true
        ByteBuf read = channel.readInbound();
        System.out.println(buf.readSlice(3) == read);//false
        read.release();

        read = channel.readInbound();
        System.out.println(buf.readSlice(3) == read);//false
        read.release();

        read = channel.readInbound();
        System.out.println(buf.readSlice(3) == read);//false
        read.release();

        System.out.println(channel.readInbound() == null);//true
        buf.release();
    }

    @Test
    public void testFixedFrameDecoder() {
        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(4));
        //测试入站写入
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 10; i++) {
            buf.writeByte(i);
        }
        ByteBuf in = buf.duplicate();
        //入站写入3个字节，此时 Decoder 会缓存这些数据，并没有转发这些数据到下一个 ChannelHandler
        assertFalse(channel.writeInbound(in.readBytes(3)));
        //入站写入7个字节，加上之前写入的3个字节，Decoder 转发其中前8个字节，分为2组转发给下一个 ChannelHandler，剩余2个字节仍被缓存
        assertTrue(channel.writeInbound(in.readBytes(7)));
        assertTrue(channel.finish());  //向通道发送结束信号
        //测试入站读取
        //由上面的写入过程可以估计，前2次都可以读取到值，第3次读取为空值
        ByteBuf read = channel.readInbound();
        assertEquals(read, buf.readSlice(4));
        read.release();
        read = channel.readInbound();
        assertEquals(read, buf.readSlice(4));
        read.release();
        read = channel.readInbound();
        assertNull(read);

    }

}
