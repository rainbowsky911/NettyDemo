package org.netty.example.codec.agreement;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.netty.demo.helloword.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Title: ServerTestHandler
 * @Description:
 * @author: zdw
 * @date: 2022/2/28 17:48
 */
public class ServerTestHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        User user=(User)msg;
        System.out.println(user.toString());
    }
}
