package org.netty.example.codec;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import org.netty.example.chat.SimpleChatClient;
import org.netty.example.chat.SimpleChatClientInitializer;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Client {
	public static void main(String[] args) throws Exception{
		new Client("localhost", 9998).run();
	}

	private final String host;
	private final int port;

	public Client(String host, int port){
		this.host = host;
		this.port = port;
	}
	public void run() throws Exception{
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap  = new Bootstrap()
					.group(group)
					.channel(NioSocketChannel.class)
					.handler(new ClientChanneInitializer());
			Channel channel = bootstrap.connect(host, port).sync().channel();
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			while(true){
				channel.writeAndFlush(in.readLine() + "\r\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}

	}

	/**
	 * 网络事件处理器
	 */
	private class ClientChanneInitializer extends
			ChannelInitializer<SocketChannel> {
		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			ch.pipeline().addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
			// 增加自定义的编码器和解码器
			ch.pipeline().addLast(new IntegerToByteEncoder());
			ch.pipeline().addLast(new ByteToIntegerDecoder());
			// 客户端的处理器
			ch.pipeline().addLast(new ClientHandler());
		}

	}


}
