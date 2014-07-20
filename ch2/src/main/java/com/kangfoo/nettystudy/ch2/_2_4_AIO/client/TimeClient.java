package com.kangfoo.nettystudy.ch2._2_4_AIO.client;

/**
 * User: kangfoo-mac
 * Date: 14-7-20
 * Time: 下午2:18
 */
public class TimeClient {
    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                // 采用默认值
            }
        }
        new Thread(new AsyncTimeClientHandler("127.0.0.1", port),  "AIO-AsyncTimeClientHandler-001").start();

    }
}
