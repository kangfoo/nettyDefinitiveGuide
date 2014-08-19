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
public abstract class BaseTCPSampler extends AbstractJavaSamplerClient {

    public static final String PARAM_IP = "ip";
    public static final String PARAM_PORT = "port";
    public static final String VAR_IP = "${ip}";
    public static final String VAR_PORT = "${port}";
    protected BaseClient client;

    public void addParameter(Arguments params) {
    }

    /**
     * Jmeter获取消息参数，默认配置ip和port两个参数
     * 如果子类有更多参数，调用super.getDefaultParameters()获取Arguments后，继续设置其他方法
     */
    @Override
    public Arguments getDefaultParameters() {
        Arguments params = new Arguments();
        params.addArgument(PARAM_IP, VAR_IP);
        params.addArgument(PARAM_PORT, VAR_PORT);
        addParameter(params);
        return params;
    }

    /**
     * runTest的前置方法
     */
    @Override
    public void setupTest(JavaSamplerContext context) {
        String ip = context.getParameter(PARAM_IP);
        String port = context.getParameter(PARAM_PORT);
        this.client = NetworkClientHolder.getClient(ip, port);
        System.out.println("thread--->" + Thread.currentThread().getId() + " client--->" + client);
    }

    /**
     * Jmeter调用，用于实际的测试
     */
    @Override
    public SampleResult runTest(JavaSamplerContext context) {
        SampleResult sample = getSample();
        sample.sampleStart();
        try {
            MessageLite response = doTest();
            String msg = response == null ? "" : response.toString();
            sample.setResponseMessage(msg);
            sample.setSuccessful(true);
        } catch (Exception e) {
            sample.setSuccessful(false);
            e.printStackTrace();
        } finally {
            sample.sampleEnd();
        }
        return sample;
    }

    /**
     * 获取本Sample的标签，子类实现
     */
    public abstract String getLabel();

    /**
     * 获取一个带标签的Sample
     */
    public SampleResult getSample() {
        SampleResult sample = new SampleResult();
        sample.setSampleLabel(getLabel());
        return sample;
    }

    /**
     * Jmeter调用，用于
     */
    @Override
    public void teardownTest(JavaSamplerContext context) {
    }

    /**
     * 需实现，具体测试的方法，调用client的send/sendWithBack发送请求
     * 如无返回，放回null即可
     */
    public abstract MessageLite doTest() throws InvalidProtocolBufferException;
}
