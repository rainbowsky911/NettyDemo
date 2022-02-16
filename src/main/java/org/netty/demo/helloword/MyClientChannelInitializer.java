package org.netty.demo.helloword;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.Charset;

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
public class MyClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) {
        //1.拆包器
        //ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,5,4));
       // ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
        //2.自定义编码器
       /* ch.pipeline().addLast(new MyDecoder());
        ch.pipeline().addLast(new MyEncoder());*/


        ch.pipeline().addLast(new StringDecoder(Charset.forName("GBK")));
        // 解码转String，注意调整自己的编码格式GBK、UTF-8
        ch.pipeline().addLast(new StringEncoder(Charset.forName("GBK")));
        //3.业务处理Handler
        ch.pipeline().addLast(new ClientTestHandler(new Bootstrap()));


    }

}
