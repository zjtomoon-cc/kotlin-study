package com.itjoin.pro_netty.asyn;
/**
 * 子线程，此线程模拟netty的异步结果响应
 * @author 夜行侠
 */
public class SubThread  extends Thread{
	private RequestFuture request;
	//结果在main方法中new出来做模拟
	public SubThread(RequestFuture request) {
		this.request = request;
	}
	@Override
	public void run() {
		//模拟额外线程获取响应结果
		Response resp = new Response();
		/**
		 * 此处id为请求id，模拟服务器接收到请求后，
		 * 拿请求id直接赋值给响应对象id
		*/
		resp.setId(request.getId());
		//响应结果赋值
		resp.setResult("server response"+Thread.currentThread().getId());
		//子线程模拟睡眠1s
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//此处返回响应结果给主线程
		RequestFuture.received(resp);
	}
	

}
