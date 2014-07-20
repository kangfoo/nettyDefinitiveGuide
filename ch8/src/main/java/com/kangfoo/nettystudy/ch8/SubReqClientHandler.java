package com.kangfoo.nettystudy.ch8;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;

/**
 * User: kangfoo-mac
 * Date: 14-7-20
 * Time: 下午7:11
 */
public class SubReqClientHandler extends ChannelHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 10; i++) {
            ctx.write(subbuilder(i));
        }
        ctx.flush();
    }

    private SubscribeReqProto.SubscribeReq subbuilder(int i) {
        SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();

        builder.setProductName("Netty 权威指南");
        builder.setSubReqID(i);
        builder.setUserName("kangfoo");

        List<String> adds = new ArrayList<String>();
        adds.add("cd");
        adds.add("sh");
        adds.add("nb");
        builder.addAllAddress(adds);

        return builder.build();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Receive server response : [" + msg + "]");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
