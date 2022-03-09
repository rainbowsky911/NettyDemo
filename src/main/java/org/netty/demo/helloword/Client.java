package org.netty.demo.helloword;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

public class Client {
    public static void main(String[] args) {
        new Client().connect("127.0.0.1", 7397);
    }

    private void connect(String inetHost, int inetPort) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
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
                    ch.pipeline().addLast(new ClientTestHandler(b));
                }

            });
            ChannelFuture f = b.connect(inetHost, inetPort).sync();
            System.out.println("itstack-demo-netty client start done. {关注公众号：bugstack虫洞栈，获取源码}");


            //实例：延迟 1 秒钟，每个 15 秒钟往服务端发送一次 hello world。
            //心跳检测http://www.imooc.com/wiki/nettylesson/netty25.html
//            f.channel().eventLoop().scheduleWithFixedDelay(() -> {
//                f.channel().writeAndFlush("hello world!");
//            }, 1, 1, TimeUnit.SECONDS);

            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
