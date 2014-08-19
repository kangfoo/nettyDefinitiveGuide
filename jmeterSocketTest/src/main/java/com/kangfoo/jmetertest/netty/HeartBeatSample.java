package com.kangfoo.jmetertest.netty;

/**
 * User: kangfoo-mac
 * Date: 14-8-20
 * Time: 上午1:08
 */
public class HeartBeatSample extends BaseTCPSampler {
    @Override
    public String getLabel() {
        return "HeartBeatSample";
    }

    @Override
    public MessageLite doTest() throws InvalidProtocolBufferException {
//        HeartBeat_C2S.Builder request = HeartBeat_C2S.newBuilder();
//        request.setTimestamp(System.currentTimeMillis());
//        ClientMessage cm = new ClientMessage();
//        cm.setContent(request.build().toByteArray());
//        cm.setName("HeartBeat");
//        ClientMessage sm = client.sendWaitBack(cm);
//        HeartBeat_S2C response = HeartBeat_S2C.parseFrom(sm.getContent());
//        return response;
        return null;
    }
}
