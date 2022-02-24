package org.netty.demo.unit.test2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Title: 入站编码器
 * @Description: // 用于测试的 Encoder，将读取的 Integer 绝对值化
 * @author: zdw
 * @date: 2022/2/24 11:10
 */
public class AbsIntegerEncoder extends MessageToMessageEncoder<ByteBuf> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        while (msg.readableBytes() >= 4) {
            //从输入的ByteBuf中读取下一个整数，并且计算其绝对值
            int value = Math.abs(msg.readInt());
            //将该整数写入到编码消息的List中
            out.add(value);
        }
    }
}
