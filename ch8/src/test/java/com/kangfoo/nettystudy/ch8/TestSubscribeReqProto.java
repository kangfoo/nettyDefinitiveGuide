package com.kangfoo.nettystudy.ch8;

import com.google.protobuf.InvalidProtocolBufferException;

import java.util.ArrayList;
import java.util.List;

/**
 * User: kangfoo-mac
 * Date: 14-7-20
 * Time: 下午7:29
 */
public class TestSubscribeReqProto {

    private static byte[] encode(SubscribeReqProto.SubscribeReq req) {
        return req.toByteArray();
    }

    private static SubscribeReqProto.SubscribeReq decode(byte[] body) throws InvalidProtocolBufferException {
        return SubscribeReqProto.SubscribeReq.parseFrom(body);
    }

    private static SubscribeReqProto.SubscribeReq createSubscribeReq() {
        SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
        builder.setSubReqID(1);
        builder.setUserName("kangfoo");
        builder.setProductName("Netty 权威指南");

        List<String> adds = new ArrayList<String>();
        adds.add("cd");
        adds.add("sh");
        adds.add("nb");

        builder.addAllAddress(adds);

        return builder.build();
    }

    public static void main(String args[]) throws InvalidProtocolBufferException {
        SubscribeReqProto.SubscribeReq req = createSubscribeReq();
        System.out.println("Before encode : " + req.toString());
        SubscribeReqProto.SubscribeReq req2 = decode(encode(req));
        System.out.println("after encode : " + req2.toString()); // 原书中此处是否应该为 req2.toStirng 而不是 req.toString
        System.out.println("Assert equal : --> " + req2.equals(req));
    }

}
