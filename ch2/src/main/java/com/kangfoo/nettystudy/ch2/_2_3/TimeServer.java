package com.kangfoo.nettystudy.ch2._2_3;

import java.io.IOException;

/**
 * User: kangfoo-mac
 * Date: 14-7-20
 * Time: 上午3:01
 */
public class TimeServer {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                // 采用默认值
            }
        }
        MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);
        new Thread(timeServer, "NIO-MultiplexerTimeServer-001").start();    // 启动一个多路复用器 Selector。
    }
}
