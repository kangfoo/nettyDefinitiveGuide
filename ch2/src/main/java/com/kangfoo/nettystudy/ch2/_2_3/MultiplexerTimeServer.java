package com.kangfoo.nettystudy.ch2._2_3;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * User: kangfoo-mac
 * Date: 14-7-20
 * Time: 上午3:02
 * <p/>
 * 用于处理多个客户端的并发接入。复制轮询多路复用器 Selector.
 */
public class MultiplexerTimeServer implements Runnable {

    private Selector selector;
    private ServerSocketChannel servChannel;
    private volatile boolean stop;

    /**
     * 初始化多路复用器、绑定监听端口
     *
     * @param port
     */
    public MultiplexerTimeServer(int port) {
        try {
            selector = Selector.open();

            servChannel = ServerSocketChannel.open();
            servChannel.configureBlocking(false);  // 设置为异步阻塞模式
            servChannel.socket().bind(new InetSocketAddress(port), 1024);// 设置 backlog =1024  ， requested maximum length of the queue of incoming connections.
            servChannel.register(selector, SelectionKey.OP_ACCEPT); // 设置 操作位为 ACCEPT
            System.out.println("The time server is start in port : " + port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

    }

    @Override
    public void run() {
        /*
         * 循环遍历 selector ，休眠 1s。
         */
        while (!stop) {
            try {
                selector.select(1000);
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectedKeys.iterator();
                SelectionKey key = null;

                while (it.hasNext()) {
                    key = it.next();
                    it.remove();

                    try {
                        handleInput(key);
                    } catch (Exception e) {
                        if (key != null) {
                            key.cancel();
                            if (key.channel() != null) {
                                key.channel().close();
                            }
                        }
                    }
                }


            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        // 多路复用器关闭后，所有注册在上面的Channel和Pipe等资源都会被自动去注册并关闭，所以不需要重复释放资源
        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void handleInput(SelectionKey key) throws IOException {
        if (key.isValid()) {
            // 处理新接入的请求消息
            if (key.isAcceptable()) {
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                SocketChannel sc = ssc.accept();    // 类型为  ACCEPT 建立 连接（相当于TCP 3 次握手），
                sc.configureBlocking(false);

                sc.register(selector, SelectionKey.OP_READ); // add the new connection to the selector

            }

            if (key.isReadable()) {
                // read the data
                SocketChannel sc = (SocketChannel) key.channel();
                ByteBuffer readBuffer = ByteBuffer.allocate(1024); // 1MB 的缓冲区
                int readBytes = sc.read(readBuffer); // 读取请求流
                if (readBytes > 0) {
                    readBuffer.flip();//将缓冲区当前的 limit 设置为 position , position 设置为 0 ， 用于后续对缓冲区的读取操作。

                    byte[] bytes = new byte[readBuffer.remaining()];  // 根据缓冲区可读的数组复制到新创建的字节数组中
                    readBuffer.get(bytes);

                    String body = new String(bytes, "UTF-8");
                    System.out.println("The time server receive order : " + body);
                    String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
                    doWrite(sc, currentTime);
                } else if (readBytes < 0) {
                    // 对端链路关闭
                    key.cancel();
                    sc.close();
                } else {
                    //  读到 0 字节， 忽略
                }

            }
        }
    }

    private void doWrite(SocketChannel sc, String response) throws IOException {
        if (response != null && response.trim().length() > 0) {
            byte[] bytes = response.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();

            sc.write(writeBuffer);
        }
    }

    public void stop() {
        this.stop = true;
    }
}
