package org.netty.demo.unit.test1;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * Created by XiuYin.Cui on 2018/7/13.
 */
public class Encoder extends MessageToMessageEncoder<ByteBuf> {


    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        while (msg.readableBytes() >= 4){
            int abs = Math.abs(msg.readInt())-1;
            out.add(abs);
        }
    }
}
