package org.netty.demo.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Map;

/**
 * @Title: ServerLoginHandler
 * @Description:
 * @author: zdw
 * @date: 2022/2/15 12:47
 */
public class ServerLoginHandler extends ChannelInboundHandlerAdapter {
    //1.读取客户端发送过来的数据
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //1.转换ByteBuf
        ByteBuf buffer=(ByteBuf)msg;
        //2.定义一个byte数组，长度是ByteBuf的可读字节数
        byte[] bytes=new byte[buffer.readableBytes()];
        //3.往自定义的byte[]读取数据
        buffer.readBytes(bytes);

        //4.对象流反序列化
        ByteArrayInputStream is=new ByteArrayInputStream(bytes);
        ObjectInputStream iss=new ObjectInputStream(is);
        Map<String,String> map=(Map<String,String>)iss.readObject();

        //5.关闭流
        is.close();
        iss.close();

        //6.认证账号、密码，并且响应
        String username=map.get("username");
        String password=map.get("password");
        if(username.equals("admin")&&password.equals("123456")){
            ctx.channel().writeAndFlush(Unpooled.copiedBuffer("success".getBytes()));
        }else{
            ctx.channel().writeAndFlush(Unpooled.copiedBuffer("error".getBytes()));
            ctx.channel().closeFuture();
        }
    }
}