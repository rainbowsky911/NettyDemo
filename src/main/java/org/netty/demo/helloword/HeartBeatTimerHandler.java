package org.netty.demo.helloword;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.TimeUnit;

/**
 * @Title: HeartBeatTimerHandler
 * @Description:
 * @author: zdw
 * @date: 2022/2/15 19:38
 */
public class HeartBeatTimerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        scheduleSendHeartBeat(ctx);
    }


    private void scheduleSendHeartBeat(ChannelHandlerContext ctx) {

        ctx.executor().schedule(() -> {
            if (ctx.channel().isActive()) {
                ctx.writeAndFlush("I'm alive");
            }
        }, 3, TimeUnit.SECONDS);
    }

}