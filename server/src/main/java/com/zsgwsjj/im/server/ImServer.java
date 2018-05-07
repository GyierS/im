package com.zsgwsjj.im.server;

import com.zsgwsjj.im.model.IMMessage;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.Scanner;

/**
 * @author : jiang
 * @time : 2018/4/25 14:40
 */
public class ImServer implements Runnable {
    private ImServerHandler serverHandler = new ImServerHandler();

    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline()
                                    .addLast(new ObjectDecoder(1024 * 1024, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())))
                                    .addLast(new ObjectEncoder())
                                    .addLast(new ImServerHandler());
                        }
                    });
            ChannelFuture f = b.bind(8080).sync();
            System.out.println("-----Wait for connect!-----");
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new ImServer().start();
    }

    private void start() {
        new Thread(this).start();
        runServerCMD();
    }

    private void runServerCMD() {
        IMMessage message = new IMMessage()
                .setFrom(10000001L)
                .setCreateTime(System.currentTimeMillis() / 1000);
        Scanner scanner = new Scanner(System.in);
        do {
            message.setBody(scanner.nextLine());
        }
        while (serverHandler.sendMsg(message));
    }
}
