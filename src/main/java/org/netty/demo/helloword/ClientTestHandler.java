package org.netty.demo.helloword;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.nio.charset.Charset;
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
       // scheduleSendHeartBeat(ctx);


       /* for (int i = 0; i < 1000; i++) {
            User user = new User();
            user.setName(i + "->zwy");
            user.setAge(18);

            //注意，这里直接写user对象，无需再手工转换字节流了，编码器会自动帮忙处理。
            ctx.channel().writeAndFlush(user);
        }*/

        String string = "faker";
        //注意，这里直接写user对象，无需再手工转换字节流了，编码器会自动帮忙处理。
       // ctx.writeAndFlush(string);
    }




    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(workerGroup);
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.AUTO_READ, true);
        //b.handler(new MyClientChannelInitializer());
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {

                ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));


                ch.pipeline().addLast(new StringDecoder(Charset.forName("GBK")));
                // 解码转String，注意调整自己的编码格式GBK、UTF-8
                ch.pipeline().addLast(new StringEncoder(Charset.forName("GBK")));
                //定时发送空包
                ch.pipeline().addLast(new HeartBeatTimerHandler());

                //断线重连Handler
                ch.pipeline().addLast(new ClientTestHandler(new Bootstrap()));
            }

        });

        bootstrap.connect("127.0.0.1",7397).sync();
    }

    private void scheduleSendHeartBeat(ChannelHandlerContext ctx) {
        ctx.executor().schedule(() -> {
            if (ctx.channel().isActive()) {
                ctx.writeAndFlush("hello world !");
            }
        }, 3, TimeUnit.SECONDS);
    }

}
