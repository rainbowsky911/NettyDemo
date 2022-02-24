package org.netty.demo.unit.test1;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.junit.jupiter.api.Test;
import org.netty.demo.echo.ClientLoginHandler;
import org.netty.demo.echo.ServerLoginHandler;
import org.netty.demo.unit.codecs.ByteToIntegerDecoder;
import org.netty.demo.unit.codecs.IntegerToByteEncoder;

import java.nio.charset.Charset;

/**
 * @Title: InboundTest
 * @Description:
 * @author: zdw
 * @date: 2022/2/15 10:48
 */

public class InboundTest {

    public static void main(String[] args) {
        echoClientHandlerTest();
    }


    /**
     * 测试登录
     */
    @Test
    public void testLogin() {
        // 1、创建 EmbeddedChannel，并加入要测试ChannelHandler
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(
                new ClientLoginHandler(),
                new ServerLoginHandler()
        );
        //2、写入入站数据，验证入站逻辑
        ByteBuf buf = Unpooled.copiedBuffer("Test EchoHandler input", Charset.forName("utf-8"));
        embeddedChannel.writeInbound(buf);
        if (buf.refCnt() != 0) {
            buf.release();
        }
        //3、写入出站数据，验证出站逻辑
        ByteBuf buf1 = Unpooled.copiedBuffer("Test EchoHandler output", Charset.forName("utf-8"));
        embeddedChannel.writeOutbound(buf1);
        if (buf1.refCnt() != 0) {
            buf1.release();
        }
    }


    public static void echoClientHandlerTest() {

        // 1、创建 EmbeddedChannel，并加入要测试ChannelHandler
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(
//                new StringEncoder(),
//                new StringDecoder(),
                new IntegerToByteEncoder(),
                new ByteToIntegerDecoder()
//                new InboundHandler1(),
//                new InboundHandler2()
                //new OutboundHandler1(),
                //new OutboundHandler2()
        );
        //2、写入入站数据，验证入站逻辑
        ByteBuf buf = Unpooled.copiedBuffer("4396", Charset.forName("utf-8"));
        embeddedChannel.writeInbound(buf);
        if (buf.refCnt() != 0) {
            buf.release();
        }
        //3、写入出站数据，验证出站逻辑
        ByteBuf buf1 = Unpooled.copiedBuffer("4399", Charset.forName("utf-8"));
        embeddedChannel.writeOutbound(buf1);
        if (buf1.refCnt() != 0) {
            buf1.release();
        }
    }

}

class InboundHandler1 extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("inbound1>>>>>>>>>");

        //往下传递
        super.channelRead(ctx, msg);
    }
}

class InboundHandler2 extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("inbound2>>>>>>>>>");

        //传递到OutboundHandler
        ctx.channel().writeAndFlush("hello world");
    }


}

class OutboundHandler1 extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println("outbound1>>>>>>>>>");
        //往下流转
        super.write(ctx, msg, promise);
    }
}

class OutboundHandler2 extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println("outbound2>>>>>>>>>");
        //往下流转
        super.write(ctx, msg, promise);
    }
}