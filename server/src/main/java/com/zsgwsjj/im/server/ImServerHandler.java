package com.zsgwsjj.im.server;

import com.zsgwsjj.im.model.IMMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : jiang
 * @time : 2018/4/25 14:35
 */
public class ImServerHandler extends ChannelInboundHandlerAdapter {

    private static ConcurrentHashMap<Long, ChannelHandlerContext> onlineMap = new ConcurrentHashMap<>();
    private ChannelHandlerContext ctx;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.err.println("服务端Handler创建...");
        super.handlerAdded(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        System.out.println("-----" + ctx.channel().remoteAddress() + " has connected!!!-----");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("-----Got message!-----");
        IMMessage message = (IMMessage) msg;
        Long fromUid = message.getFrom();
        onlineMap.putIfAbsent(fromUid, ctx);
        if (message.getTo() == 10000001) {
            message.setBody("您已上线！");
            ctx.writeAndFlush(message);
            return;
        }
        ChannelHandlerContext c = onlineMap.get(message.getTo());
        if (c == null) {
            message.setBody("对方不在线！");
            onlineMap.get(message.getFrom()).writeAndFlush(message);
        } else {
            c.writeAndFlush(message);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    public boolean sendMsg(IMMessage msg) {
        System.out.println("服务器推送消息：" + msg);
        for (Map.Entry<Long, ChannelHandlerContext> entry : onlineMap.entrySet()) {
            entry.getValue().writeAndFlush(msg);
        }
        return !msg.getBody().equals("q");
    }
}
