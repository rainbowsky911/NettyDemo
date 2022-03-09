package org.netty.demo.helloword;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

public class Server {
    public static void main(String[] args) {
        new Server().bing(7397);
    }

    private void bing(int port) {
        //配置服务端NIO线程组
        EventLoopGroup parentGroup = new NioEventLoopGroup(); //NioEventLoopGroup extends MultithreadEventLoopGroup Math.max(1, SystemPropertyUtil.getInt("io.netty.eventLoopThreads", NettyRuntime.availableProcessors() * 2));
        EventLoopGroup childGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)    //非阻塞模式
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //.childHandler(new MyServerChannelInitializer())
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        //断线重连
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 服务端心跳检测
                            ChannelPipeline pipeline = ch.pipeline();

                            pipeline.addLast(new ReadTimeoutHandler(5, TimeUnit.SECONDS));


                            pipeline.addLast(new LoggingHandler(LogLevel.DEBUG));

                            //5秒钟之内没有 读事件 则断开连接

                            //字符串解码器
                            pipeline.addLast(new StringDecoder(Charset.forName("GBK")));

                            //字符串编码器
                            pipeline.addLast(new StringEncoder(Charset.forName("GBK")));

                            //业务Handler
                            pipeline.addLast(new HeartBeatHandler());
                        }
                    })

            ;
            ChannelFuture f = b.bind(port).sync();
            System.out.println("itstack-demo-netty server start done. {关注公众号：bugstack虫洞栈，获取源码}");
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            childGroup.shutdownGracefully();
            parentGroup.shutdownGracefully();
        }

    }


}
