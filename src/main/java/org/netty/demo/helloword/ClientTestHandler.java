package org.netty.demo.helloword;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.TimeUnit;

/**
 * @Title: ClientTestHandler
 * @Description:
 * @author: zdw
 * @date: 2022/2/15 15:27
 */
public class ClientTestHandler extends ChannelInboundHandlerAdapter {

    private Bootstrap bootstrap;
    ClientTestHandler(Bootstrap bootstrap){
        this.bootstrap=bootstrap;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //客户端定时发送空包
        scheduleSendHeartBeat(ctx);


       /* for (int i = 0; i < 1000; i++) {
            User user = new User();
            user.setName(i + "->zwy");
            user.setAge(18);

            //注意，这里直接写user对象，无需再手工转换字节流了，编码器会自动帮忙处理。
            ctx.channel().writeAndFlush(user);
        }*/

        String string = "faker";
        //注意，这里直接写user对象，无需再手工转换字节流了，编码器会自动帮忙处理。
        ctx.writeAndFlush(string);
    }




    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        bootstrap.connect("127.0.0.1",7397).sync();
    }

    private void scheduleSendHeartBeat(ChannelHandlerContext ctx) {
        ctx.executor().schedule(() -> {
            if (ctx.channel().isActive()) {
                ctx.writeAndFlush("zz");
            }
        }, 3, TimeUnit.SECONDS);
    }

}
