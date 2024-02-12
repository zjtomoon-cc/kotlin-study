package com.itjoin.pro_netty.asyn;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
public class RequestFuture {
	//请求缓存类，key为每次请求id，value为请求对象
	public static Map<Long,RequestFuture> futures = new ConcurrentHashMap<Long,RequestFuture>();
	//每次请求id，id可以设置原子性增长
	private long  id;
	private String path;
	//请求参数
	private Object request;
	//响应结果
	private Object result;
	//超时时间默认1s
	private long timeout=5000;
	//自增id
	private static final AtomicLong aid=new AtomicLong(1);
	public RequestFuture() {
		//当前值新增1并返回结果给id
		id = aid.incrementAndGet();
		//构建请求时，需把请求加进缓存中
		addFuture(this);
	}
	//把请求追到到缓存中
	public static void addFuture(RequestFuture future) {
		futures.put(future.getId(), future);
	}
	/**同步获取响应结果*/
	public Object get() {
		/**此处可以把同步块跟wait换成 ReentrantLock与Condition*/
		synchronized (this) {
		 while(this.result==null) {
			 try {
				 /**主线程默认等待1s，然后再查看是否获取到结果*/
					this.wait(timeout);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		 }
			
		}
		return this.result;
	}
	/**异步线程返回结果给主线程*/
	public static void received(Response resp) {
		RequestFuture future = futures.remove(resp.getId());
		//设置响应结果
		if(future!=null) {
			 future.setResult(resp.getResult());
				/**通知主线程*/
				synchronized (future) {
					future.notify();
				}
		}
		   
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Object getRequest() {
		return request;
	}
	public void setRequest(Object request) {
		this.request = request;
	}
	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
}
