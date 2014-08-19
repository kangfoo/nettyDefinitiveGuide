package com.kangfoo.jmetertest.netty;

/**
 * User: kangfoo-mac
 * Date: 14-8-20
 * Time: 上午12:24
 */
public class NetworkClientHolder {

    /**
     * 这里使用ThradLocal存储BaseClient
     * 方便一轮测试的每个sample都是由同一个socketChannel发送
     * 更真实的模拟用户
     */
    private static ThreadLocal<BaseClient> clientHolder = new ThreadLocal<BaseClient>();

    public static BaseClient getClient(String ip, String port) {
        BaseClient client = clientHolder.get();
        if (null == client) {
            client = new BaseClient(ip, port);
            client.connect();
            clientHolder.set(client);
        }
        return client;
    }
}
