package org.netty.demo.helloword;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;

/**
 * @Title: MyDecoder
 * @Description:
 * @author: zdw
 * @date: 2022/2/15 15:26
 */
public class MyDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(
            ChannelHandlerContext channelHandlerContext,
            ByteBuf byteBuf, List<Object> list) throws Exception {

        //1.根据协议分别取出对应的数据
        int tag=byteBuf.readInt();//标识符
        byte code=byteBuf.readByte();//指令
        int len=byteBuf.readInt();//长度
        byte[] bytes=new byte[len];//定义一个字节数据，长度是数据的长度
        byteBuf.readBytes(bytes);//往字节数组读取数据

        //2.通过对象流来转换字节流，转换成User对象
        ByteArrayInputStream is=new ByteArrayInputStream(bytes);
        ObjectInputStream iss=new ObjectInputStream(is);
        User user=(User)iss.readObject();
        is.close();
        iss.close();

        list.add(user);
    }
}
