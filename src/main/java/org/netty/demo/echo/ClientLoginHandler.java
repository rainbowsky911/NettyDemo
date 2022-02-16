package org.netty.demo.echo;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @Title: ClientLoginHandler
 * @Description:
 * @author: zdw
 * @date: 2022/2/15 12:46
 */
public class ClientLoginHandler extends ChannelInboundHandlerAdapter {
    //1.通道激活的时候，发送账号、密码
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Map<String,String> map=new HashMap<String,String>();
        map.put("username","admin");
        map.put("password","1234567");

        //对象流序列化Map
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(map);
        byte[] bytes=os.toByteArray();

        //关闭流
        oos.close();
        os.close();

        //发送
        ctx.channel().writeAndFlush(Unpooled.copiedBuffer(bytes));
    }
}