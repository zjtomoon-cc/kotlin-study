package com.itjoin.pro_netty.client;

import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;

import com.alibaba.fastjson.JSONObject;
import com.itjoin.pro_netty.asyn.RequestFuture;

import com.itjoin.pro_netty.asyn.Response;
import com.itjoin.pro_netty.controller.UserController;
import com.itjoin.pro_netty.controller.UserControllerI;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
public class NettyClient {
//	public static EventLoopGroup group=null;
 //开启一个线程组
public static EventLoopGroup group = new NioEventLoopGroup(100);
//	public static Bootstrap bootstrap;
//	static{
//
//	}
	public static Bootstrap getBootstrap(){
		//客户端启动辅助类
		Bootstrap bootstrap = new Bootstrap();
		//设置socket通道
		bootstrap.channel(NioSocketChannel.class);
		bootstrap.group(group);
		//设置内存分配器
		bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
		final ClientHandler handler = new ClientHandler();
		//把promise对象赋给handler，用于获取返回服务器响应结果
//		handler.setPromise(promise);
		//把handler加入到管道中
		bootstrap .handler(new ChannelInitializer<NioSocketChannel>() {
			@Override
			protected void initChannel(NioSocketChannel ch)
					throws Exception {
				//新增，前缀为4个字节大小的int类型作为长度的解码器
				//第一个参数是包最大大小，第二个参数是长度值偏移量，
				// 由于编码时长度值在最前面，无偏移，此处设置为0
				//第三个参数长度值占用的字节数，
				//第四个参数是长度值的调节，假如请求包的大小是20个字节，
				// 长度值没包含本身的话应该是20，假如长度值包含了本身就是24，
				// 需要调整4个字节
				//第五个参数,解析时候需要跳过的字节长,此处为4，跳过长度值字节数
				ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,
						0, 4, 0, 4));
				//把接收到的Bytebuf数据包转换成String
				ch.pipeline().addLast(new StringDecoder());
				//业务逻辑处理handler
				ch.pipeline().addLast(handler);
				//在消息体前面新增4个字节的长度值,第一个参数长度值字节数大小，
				//第二个参数长度值是否要包含长度值本身大小
				ch.pipeline().addLast(new LengthFieldPrepender(4, false));
				//把字符串消息转换成ByteBuf
				ch.pipeline().addLast(new StringEncoder(Charset.forName("utf-8")));
			}
		});
		return bootstrap;
	}

	
	public  Object sendRequest(Object msg,String path) throws Exception {
		try {
			//新建一个Promise对象
//			Promise <Response> promise = new DefaultPromise <>(group.next());
			//业务handler
			//构建request
			RequestFuture request = new RequestFuture();
			request.setPath(path);
			//设置请求id，此处请求id可以设置为自动自增模式,可以采用AtomicLong类的incrementAndGet方法
			//此处在RequestFuture类里已改成自增id
//			request.setId(1);
			//请求消息内容，此处内容可以任意Java对象
			request.setRequest(msg);
			//转换成JSON发送给编码器StringEncode,
			//StringEncode编码器再发送给LengthFieldPrepender长度编码器，最终写到tcp缓存中并传送给客户端
			String requestStr = JSONObject.toJSONString(request);
			ChannelFuture future = ChannelFutureManager.get();
//			ChannelFuture future =getBootstrap().connect("localhost",8080);
					future.channel().writeAndFlush(requestStr);
			//同步等待响应结果，当promise有值了才会继续往下执行
			Object result = request.get();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
	}

	public static Object sendRequest(RequestFuture request) throws Exception {
		try {
			String requestStr = JSONObject.toJSONString(request);
//			ChannelFuture future = ChannelFutureManager.get();
			ChannelFuture future =getBootstrap().connect("127.0.0.1",8991).sync();
			future.channel().writeAndFlush(requestStr);
			//同步等待响应结果，当promise有值了才会继续往下执行
			Object result = request.get();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	public static void main(String[] args) throws Exception {
		 NettyClient client = new NettyClient();
		for(int i=0;i<100;i++) {
			RequestFuture request = new RequestFuture();
			//把接口类名+方法名组装成path
			request.setPath(UserControllerI.class.getName()+"."+"getUserNameById");
			//设置参数
			request.setRequest(""+i);
			//请注意这种方式无法跑通代码，主要是服务端协议有变化
//			Object result = client.sendRequest("id"+i,"getUserNameById");
			Object result = client.sendRequest(request);
			System.out.println(result);
		}
	}
}
