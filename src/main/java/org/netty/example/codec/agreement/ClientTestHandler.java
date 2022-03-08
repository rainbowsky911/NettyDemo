package org.netty.example.codec.agreement;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.netty.demo.helloword.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Title: ClientTestHandler
 * @Description:
 * @author: zdw
 * @date: 2022/2/28 17:48
 */
public class ClientTestHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for(int i=0;i<1000;i++){
            User user=new User();
            user.setName(i+"->zwy");
            user.setAge(18);

            //注意，这里直接写user对象，无需再手工转换字节流了，编码器会自动帮忙处理。
            ctx.channel().writeAndFlush(user);
        }
    }
}
