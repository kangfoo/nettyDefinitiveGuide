package com.kangfoo.nettystudy.ch3.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;


/**
 * User: kangfoo-mac
 * Date: 14-7-20
 * Time: 下午2:47
 */
public class TimeServer {

    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                // TODO 请自行扩展.
            }
        }

        new TimeServer().bind(port);
    }

    public void bind(int port) {
        // NioEventLoopGroup 线程组包含一组NIO线程， 专门用于处理网络的事件。
        // 两个线程组，一个用于处理接受客户端的连接，一个用于进行 SocketChannel 的网络读取。
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(boosGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1012)
                    .childHandler(new ChildChannelHandler());

            ChannelFuture f = b.bind(port).sync();     // 绑定端口
            f.channel().closeFuture().sync();// 等待服务器端监听端口关闭

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 优雅退出，并释放线程池资源
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new TimeServerHandler());
        }
    }
}
