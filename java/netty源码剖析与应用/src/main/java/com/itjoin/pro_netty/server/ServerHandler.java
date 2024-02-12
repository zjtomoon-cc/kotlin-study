package com.itjoin.pro_netty.server;
import java.nio.charset.Charset;

import com.alibaba.fastjson.JSONObject;
import com.itjoin.pro_netty.asyn.RequestFuture;
import com.itjoin.pro_netty.asyn.Response;

import com.itjoin.pro_netty.core.Mediator;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
//Sharable注解表示此Handler对所有Channel共享，无状态，注意多线程并发
@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {
	/**
	 * 读取客户端发送的数据
	 */
	 @Override
	    public void channelRead(ChannelHandlerContext ctx, Object msg) { 
//			 if(msg instanceof ByteBuf) {
//				 //ByteBuf的toString方法把二进制数据转换成字符串，默认编码UTF-8
//				 System.out.println(((ByteBuf)msg).toString(Charset.defaultCharset()));
//			 }
//			ctx.channel().writeAndFlush("msg has recived!");
		 //获取客户端发送的请求，并转换成RequestFuture对象，
		 //由于经过StringDecoder解码器，因此msg为String类型
		 RequestFuture request = JSONObject.parseObject(msg.toString(),RequestFuture.class);
//		 //获取请求id
//		 long id = request.getId();
//		 System.out.println("请求信息为==="+msg.toString());
//		 //构建响应结果
//		 Response response = new Response();
//		 response.setId(id);
//		 response.setResult("服务器响应ok"+id);
		 Response response = Mediator.process(request);
//		 new Thread(){
//			 @Override
//			 public void run() {
//				 //把响应结果返回给客户端
//				 ctx.channel().writeAndFlush(JSONObject.toJSONString(response));
//			 }
//		 };
		 //把响应结果返回给客户端
		 ctx.channel().write(JSONObject.toJSONString(response));
		 ctx.channel().unsafe().flush();
	    }

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
	}
}
