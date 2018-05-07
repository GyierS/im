package com.zsgwsjj.im.client;

import com.zsgwsjj.im.model.IMMessage;
import com.zsgwsjj.im.util.IdGene;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.util.Scanner;

/**
 * @author : jiang
 * @time : 2018/4/25 14:55
 */
public class IMClient implements Runnable {

    private IMClientHandler clientHandler = new IMClientHandler();

    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline().addLast(
                                    new ObjectDecoder(1024, ClassResolvers.cacheDisabled(this.getClass().getClassLoader())))
                                    .addLast(new ObjectEncoder())
                                    .addLast(clientHandler);
                        }
                    });

            ChannelFuture f = b.connect("127.0.0.1", 8080).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    private void runCMD() {
        IMMessage message = new IMMessage()
                .setFrom(IdGene.geneId()).setId(IdGene.geneId());
        Scanner scanner = new Scanner(System.in);
        do {
            message.setTo(scanner.nextInt());
            message.setBody(scanner.nextLine());
        }
        while (clientHandler.sendMsg(message));
    }

    private void start() {
        new Thread(this).start();
        runCMD();
    }

    public static void main(String[] args) {
        new IMClient().start();
    }

}
