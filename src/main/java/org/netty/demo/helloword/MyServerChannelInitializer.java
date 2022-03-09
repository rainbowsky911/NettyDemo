package org.netty.demo.helloword;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * @Title: MyChannelInitializer
 * @Description:
 * @author: zdw
 * @date: 2022/2/15 16:15
 */

/**
 * 虫洞栈：https://bugstack.cn
 * 公众号：bugstack虫洞栈  ｛获取学习源码｝
 * Create by fuzhengwei on 2019
 */
public class MyServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) {

        //1.Netty内置拆包器
       // ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 5, 4));
        // ch.pipeline().addLast(new LineBasedFrameDecoder(1024));


        //5秒钟没有读事件，则断开连接
        ch.pipeline().addLast(new ReadTimeoutHandler(3, TimeUnit.SECONDS));
        //5秒钟没有写事件，则断开连接
       // ch.pipeline().addLast(new WriteTimeoutHandler(3, TimeUnit.SECONDS));


        //2.自定义解码器
        /*ch.pipeline().addLast(new MyEncoder());
        ch.pipeline().addLast(new MyDecoder());*/

        ch.pipeline().addLast(new StringDecoder(Charset.forName("GBK")));
        ch.pipeline().addLast(new StringEncoder(Charset.forName("GBK")));

        //业务Handler
        ch.pipeline().addLast(new HeartBeatHandler());

        //3.业务Handler
        ch.pipeline().addLast(new ServerTestHandler());

    }

}
