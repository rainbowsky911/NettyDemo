package org.netty.demo.helloword;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Title: ServerTestHandler
 * @Description:
 * @author: zdw
 * @date: 2022/2/15 15:29
 */
public class ServerTestHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
      /*  User user = (User) msg;
        System.out.println(user);*/


        String string = (String) msg;
        System.out.println(string);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务器启动成功");
        //    super.channelActive(ctx);
    }
}