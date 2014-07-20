package com.kangfoo.nettystudy.ch12;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.ThreadLocalRandom;

/**
 * User: kangfoo-mac
 * Date: 14-7-20
 * Time: 下午10:53
 */
public class ChineseProverbServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    //
    private static final String[] DICTIONARY = {"人生要么是一次不可思议的冒险，要么是平淡如初", "海越深，越平静；人越深，越淡然。", "用一杯水的单纯，面对一辈子的复杂。", "只要功夫深,铁杵磨成针。"};

    private String nextQuote() {
        int quoteId = ThreadLocalRandom.current().nextInt(DICTIONARY.length);
        return DICTIONARY[quoteId];
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        String req = msg.content().toString(CharsetUtil.UTF_8);
        System.out.println(req);

        if ("谚语字典查询？".equals(req)) {
            ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer("查询谚语结果：" + nextQuote(), CharsetUtil.UTF_8), msg.sender()));
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        cause.printStackTrace();
    }
}
