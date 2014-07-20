package com.kangfoo.nettystudy.ch2._2_4_AIO.server;

import java.io.IOException;

/**
 * User: kangfoo-mac
 * Date: 14-7-20
 * Time: 下午2:10
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
        AsyncTimeServerHandler timeServer = new AsyncTimeServerHandler(port);
        new Thread(timeServer, "AIO-AsyncTimeServerHandler-001").start();
    }
}

