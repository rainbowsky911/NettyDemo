package org.netty.example.chat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;

/**
 * @Title: SimpleChatClientHandler
 * @Description:
 * @author: zdw
 * @date: 2022/2/16 17:13
 */
public class SimpleChatClientHandler extends SimpleChannelInboundHandler<String> {
   /* @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        System.out.println(s);
    }*/

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(msg);

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        AttributeKey<Object> key = AttributeKey.valueOf("token");
        String value = (String) ctx.channel().attr(key).get();
        System.out.println(value);
    }
}