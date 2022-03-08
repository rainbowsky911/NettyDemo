package org.netty.demo.unit.test2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Title: ToIntegerReplayingDecoder
 * @Description:
 * @author: zdw
 * @date: 2022/2/28 2:02
 */
/**
 * Integer解码器,ReplayingDecoder实现
 */
public class ToIntegerReplayingDecoder extends ReplayingDecoder<Void> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        out.add(in.readInt());
    }
}