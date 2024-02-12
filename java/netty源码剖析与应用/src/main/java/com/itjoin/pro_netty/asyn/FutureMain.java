package com.itjoin.pro_netty.asyn;

import java.util.ArrayList;
import java.util.List;

/**
 * 主线程类，主线程模拟发送请求，开启额外线程模拟获取响应结果，
 * 并异步把响应结果返回给主线程
 * @author 夜行侠
 */
public class FutureMain {
	public static void main(String[] args) {
		//请求列表
		List<RequestFuture> reqs = new ArrayList<>();
		/**
		 * 此处用for循环模拟连续发送100个请求
		 * 异步100条线程响应结果，
		 * 当然此处大家还可以用线程池模拟构建100条线程发送请求，
		 * 然后主线程等待所有子线程获取到对应的响应结果，希望大家能对代码做相应的改造
		 */
		for(int i=1;i<100;i++) {
			//请求id
			long id = i;
			//构建请求对象
			RequestFuture req = new RequestFuture();
			req.setId(id);
			//设置请求内容
			req.setRequest("hello world");
			//把请求缓存起来
			RequestFuture.addFuture(req);
			//把请求加入到请求列表
			reqs.add(req);
			//模拟发送请求
			sendMsg(req);
			//模拟线程请求到对应的请求
			SubThread elseThread = new SubThread(req);
			elseThread.start();
		}
		for(RequestFuture req : reqs) {
			//主线程获取响应结果
			Object result = req.get();
			//输出结果
			System.out.println(result.toString());
		}
	}

	private static void sendMsg(RequestFuture req) {
		System.out.println("客户端发送数据,请求id为====="+req.getId());
	}

}
