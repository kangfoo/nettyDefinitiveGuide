package com.kangfoo.jmetertest.netty;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

/**
 * User: kangfoo-mac
 * Date: 14-8-20
 * Time: 上午12:25
 * <p/>
 * ref: http://www.blogjava.net/masfay/archive/2012/08/29/386497.html
 */
public class BaseClient {

    private String ip;
    private String port;

    private Channel channel;
    private ClientBootstrap clientBootstrap;
    private Object syn = new Object();
    private static final int Receive_Timeout = 10000;// ms
    private ClientMessage response = null;

    public BaseClient(String ip, String port) {
        this.ip = ip;
        this.port = port;
    }

    public void connect() {
        clientBootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
        clientBootstrap.setOption("tcpNoDelay", true);
        clientBootstrap.setPipelineFactory(new ClientPipelineFactory());

        while (true) {
            ChannelFuture future = clientBootstrap.connect(new InetSocketAddress(ip, Integer.parseInt(port)));
            future.awaitUninterruptibly(5000);
            if (future.isDone()) {
                channel = future.getChannel();
                if (channel != null && channel.isConnected()) {
                    break;
                }
            }
        }
    }

    public void disconnect() {
        if (channel.isConnected()) {
            channel.disconnect();
        }
    }

    public boolean isConnected() {
        return channel.isConnected();
    }

    public void close() {
        if (this.channel.isOpen()) {
            this.channel.close();
        }
        clientBootstrap.releaseExternalResources();
    }

    /**
     * 直接发送
     * @param message
     */
    public void send(ClientMessage message) {
        channel.write(message);
    }

    /**
     * 发送并阻塞等待返回
     * @param message
     * @return
     */
    public ClientMessage sendWaitBack(ClientMessage message) {
        response = null;
        try {
            channel.write(message);
            synchronized (syn) {
                try {
                    syn.wait(Receive_Timeout);
                } catch (InterruptedException e) {
                    System.err.println(e);
                }

            }

            if (null == response) {
                System.err.println("Receive response timeout");
            }

        } catch (Exception e) {
            System.err.println(e);
        }

        return response;
    }

    class ClientPipelineFactory implements ChannelPipelineFactory {

        @Override
        public ChannelPipeline getPipeline() throws Exception {
            ChannelPipeline p = Channels.pipeline();
//            p.addLast("frameDecoder", new ClientDecoder());
//            p.addLast("frameEncoder", new ClientEncoder());
            p.addLast("logicHandler", new ClientMsgHandler());

            return p;
        }
    }

    class ClientMsgHandler extends SimpleChannelHandler {
        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            Object obj = e.getMessage();
            if (obj instanceof ClientMessage) {
                ClientMessage msg = (ClientMessage) obj;
                response = msg;
                synchronized (syn) {
                    syn.notifyAll();
                }
            }
        }

        public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            System.out.println("connected server:" + ctx.getChannel());
        }

        public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            System.out.println("disconnected server:" + ctx.getChannel());
        }

        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
            System.out.println("Error in exceptionCaught:" + e.getCause());
        }
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
