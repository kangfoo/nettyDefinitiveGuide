package com.kangfoo.nettystudy.ch7;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * User: kangfoo-mac
 * Date: 14-7-20
 * Time: 下午6:11
 */
public class SubReqClient {

    public void connect(int port, String host) {
        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(
                                        // 对类加载器进行缓存。， 对象最大序列化为 1MB.
                                        new ObjectDecoder(1024 * 1024, ClassResolvers.cacheDisabled(this.getClass().getClassLoader()))
                                )
                                .addLast(new ObjectEncoder())
                                .addLast(new SubReqClientHandler());

                    }
                });


        try {
            ChannelFuture f = b.connect(host, port).sync(); // 发起异步连接操作
            f.channel().closeFuture().sync();// 等待客户端链路关闭。
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                // TODO 请自行扩展.
            }
        }

        new SubReqClient().connect(port, "localhost");
    }}
