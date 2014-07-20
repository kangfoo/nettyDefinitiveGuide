package com.kangfoo.nettystudy.ch2._2_2;

import com.kangfoo.nettystudy.ch2._2_1_BIO.TimeServerHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * User: kangfoo-mac
 * Date: 14-7-20
 * Time: 上午2:10
 *
 * 伪异步 I/O 比 普通的 BIO 模型多了个 线程池，它仍然没有解决 IO 堵塞的问题。
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
            TimeServerHandlerExecutePool singleExecutor = new TimeServerHandlerExecutePool(50, 1000);

            while (true) {

                socket = server.accept();
                singleExecutor.execute(new TimeServerHandler(socket));    // 在此处多了个 线程池模型。


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
