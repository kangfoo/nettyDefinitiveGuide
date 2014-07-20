package com.kangfoo.nettystudy.ch2._2_1_BIO;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * User: kangfoo-mac
 * Date: 14-7-20
 * Time: 上午12:59
 */
public class TimeServer {

    public static void main(String[] args) throws IOException {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                // TODO 请自行扩展.
            }
        }

        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            System.out.println("The time server is start in port : " + port);
            Socket socket = null;
            while (true) {


                /*
                 * 若没有客户端接入， 主线程一直阻塞在 accept 操作上。
                 *  java.lang.Thread.State: RUNNABLE
                 *  at java.net.PlainSocketImpl.socketAccept(Native Method)
                 *  at java.net.AbstractPlainSocketImpl.accept(AbstractPlainSocketImpl.java:398)
                 */
                socket = server.accept();
                new Thread(new TimeServerHandler(socket)).start();
            }

        } finally {
            if (server != null) {
                System.out.println("The time server close");
                server.close();
                server = null;
            }

        }

    }

}
