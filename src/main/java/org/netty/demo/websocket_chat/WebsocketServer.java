package org.netty.demo.websocket_chat;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.ImmediateEventExecutor;
import org.netty.demo.websocket_chat.ServerInitializer;

import java.net.InetSocketAddress;

/**
 * websocket server
 * 2018/11/5.
 */
public class WebsocketServer {

    private final ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
    private final EventLoopGroup group = new NioEventLoopGroup();
    int port;
    public WebsocketServer(int port){
        this.port = port;
    }

    public ChannelFuture start(){
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(group)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ServerInitializer(channelGroup));
        ChannelFuture future = bootstrap.bind(new InetSocketAddress(port));
        future.syncUninterruptibly();
        System.out.println(" server start up on port: "+port);
        return future;
    }
    public void destroy(){
        channelGroup.close();
        group.shutdownGracefully();
    }

}
