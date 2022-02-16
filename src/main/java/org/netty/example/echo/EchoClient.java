/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.netty.example.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * Sends one message when a connection is open and echoes back any received
 * data to the server.  Simply put, the echo client initiates the ping-pong
 * traffic between the echo client and server by sending the first message to
 * the server.
 */
public final class EchoClient {

    public static void main(String[] args) {
        try {
            NioEventLoopGroup group = new NioEventLoopGroup();
            Channel channel = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            //添加handler
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new LoggingHandler(LogLevel.DEBUG));
                            ch.pipeline().addLast(new StringEncoder()); //bytebuf转为字符串
                            ch.pipeline().addLast(new StringDecoder()); //bytebuf转为字符串
                            ch.pipeline().addLast(new EchoClientHandler()); //bytebuf转为字符串

                        }
                    }).connect(new InetSocketAddress("localhost", 8007))
                    .sync()
                    .channel();
            new Thread(() -> {
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    String line = scanner.nextLine();
                    if ("q".equals(line)) {
                        channel.close();
                        // log.debug("处理关闭之后操作");
                        break;
                    }
                    channel.writeAndFlush(line);
                }
            }, "input_client").start();

            //  方法①获取closeFuture对象
           /* ChannelFuture closeFuture = channel.closeFuture();
            log.debug("waiting close");
            closeFuture.sync();
            log.debug("处理关闭之后操作");*/

            ChannelFuture closeFuture = channel.closeFuture();
            closeFuture.addListener((ChannelFutureListener) future -> {
                group.shutdownGracefully();//线程组停下来
            });

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
