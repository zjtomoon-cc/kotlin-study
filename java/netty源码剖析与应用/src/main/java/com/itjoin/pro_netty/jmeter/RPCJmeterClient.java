package com.itjoin.pro_netty.jmeter;
import com.alibaba.fastjson.JSONObject;
import com.itjoin.pro_netty.controller.LoginController;
import com.itjoin.pro_netty.zookeeper.ServerChangeWatcher;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.atomic.AtomicBoolean;

public class RPCJmeterClient extends AbstractJavaSamplerClient{
	public static AtomicBoolean hasInit=new AtomicBoolean(false);
	/**
	 * 参数信息，此处可以通过Arguments的addArgument方法加上请求参数
	 * @return
	 */
	@Override
	public Arguments getDefaultParameters() {
		return null;
	}

	public static LoginController loginController;
	/**
	 * 开始测试
	 * @param context
	 * @return
	 */
	@Override
	public SampleResult runTest(JavaSamplerContext context) {
		SampleResult result = new SampleResult();
		try {
			//开始计时
			result.sampleStart();
			//请求分布式rpc服务器
			Object response  = loginController.testSecondSell("1");
			result.setResponseData(JSONObject.toJSONString(response),"utf-8");
			result.setResponseOK();
			//计时结束
			result.sampleEnd();
			//响应正常
			result.setSuccessful(true);
		} catch (Exception e) {
			e.printStackTrace();
			//响应异常
			result.setSuccessful(false);
		}
		return result;
	}
	/**
	 * 每个线程测试前执行一次，做一些初始化工作；
	 * @param context
	 */
	@Override
	public void setupTest(JavaSamplerContext context) {
		super.setupTest(context);
//		System.out.println("线程"+Thread.currentThread().getId()+"初始化===");
		//初始化Spring容器，跟TestProxyRpc的main方法内容一致
		 synchronized (hasInit){
			 if(!hasInit.getAndSet(true)){
				 System.out.println("启动容器初始化===");
				 AnnotationConfigApplicationContext springContext = new
						 AnnotationConfigApplicationContext(
						 new String[]{"com.itjoin.pro_netty.controller",
								 "com.itjoin.pro_netty.proxy"});
				 loginController= springContext.
						 getBean(LoginController.class);
				 //socket连接初始化
				 try {
					 ServerChangeWatcher.initChannelFuture();
				 } catch (Exception e) {
					 e.printStackTrace();
				 }
			 }
		 }


	}

	/**
	 * 压测结束时被调用
	 * @param context
	 */
	public void teardownTest(JavaSamplerContext context) {
//		System.out.println("======over====");
	}
}
