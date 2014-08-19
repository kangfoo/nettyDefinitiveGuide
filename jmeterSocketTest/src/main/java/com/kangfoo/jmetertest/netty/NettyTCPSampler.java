package com.kangfoo.jmetertest.netty;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

/**
 * User: kangfoo-mac
 * Date: 14-8-19
 * Time: 下午11:50
 */
public class NettyTCPSampler extends AbstractJavaSamplerClient {

    String accessToken;

    @Override
    public Arguments getDefaultParameters() {

        Arguments arguments = new Arguments();
        arguments.addArgument("url", "");
        arguments.addArgument("type", "");
        arguments.addArgument("cardId", "");
        arguments.addArgument("cardPwd", "");
        arguments.addArgument("barCode", "");
        arguments.addArgument("accessToken", "");
        return arguments;
    }

    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        String url = javaSamplerContext.getParameter("url");
        String type = javaSamplerContext.getParameter("type");
        String cardId = javaSamplerContext.getParameter("cardId");
        String cardPwd = javaSamplerContext.getParameter("cardPwd");
        String barCode = javaSamplerContext.getParameter("barCode");
        accessToken = javaSamplerContext.getParameter("accessToken");

        SampleResult result = new SampleResult();

        return result;
    }
}
