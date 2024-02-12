package com.itjoin.pro_netty.client;
import com.alibaba.fastjson.JSONObject;
import com.itjoin.pro_netty.asyn.RequestFuture;
import com.itjoin.pro_netty.asyn.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.Promise;
public class ClientHandler extends ChannelInboundHandlerAdapter {
//	private Promise<Response> promise;
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		//读取服务器返回的响应结果，并转换成Response对象，
		//由于经过了StringDecoder解码器，因此msg为String类型
		Response response = JSONObject.parseObject(msg.toString(), Response.class);
		RequestFuture.received(response);
		//设置响应结果，并唤醒主线程
//		promise.setSuccess(response);
	}
//	public Promise<Response>  getPromise() {
//		return promise;
//	}
//	public void setPromise(Promise<Response>  promise) {
//		this.promise = promise;
//	}
//	
	
}
