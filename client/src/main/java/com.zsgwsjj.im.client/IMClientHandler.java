package com.zsgwsjj.im.client;

import com.zsgwsjj.im.model.IMMessage;
import com.zsgwsjj.im.util.IdGene;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author : jiang
 * @time : 2018/4/25 14:56
 */
public class IMClientHandler extends ChannelInboundHandlerAdapter {
    private ChannelHandlerContext ctx;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("成功连接服务器！");
        this.ctx = ctx;
        IMMessage message = new IMMessage();
        message.setTo(10000001).setFrom(IdGene.geneId()).setBody("连接汇报");
        sendMsg(message);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        IMMessage recIMMessage = (IMMessage) msg;
        System.out.println("接收到数据: " + recIMMessage.toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    public boolean sendMsg(IMMessage msg) {
        System.out.println("client:" + msg);
        ctx.channel().writeAndFlush(msg);
        return !msg.getBody().equals("q");
    }
}
