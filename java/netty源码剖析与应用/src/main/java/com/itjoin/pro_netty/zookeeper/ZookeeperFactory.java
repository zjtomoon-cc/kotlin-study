package com.itjoin.pro_netty.zookeeper;
import com.itjoin.pro_netty.constant.Constants;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
public class ZookeeperFactory {
	public static CuratorFramework client;
	public static CuratorFramework create(){
		synchronized (CuratorFramework.class){
			if(client==null){
				RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
				//此处连接Zookeeper地址，生产环境上需要写到配置文件中
//			client = CuratorFrameworkFactory.newClient("localhost:2181",
//					5000,5000, retryPolicy);
				client = CuratorFrameworkFactory.newClient(Constants.zookeeperURL,
						5000,5000, retryPolicy);
				client.start();
			}
		}
		return client;
	}
	public static CuratorFramework recreate(){
		client=null;
		create();
		return client;
	}
	public static void main(String[] args) throws Exception {
		CuratorFramework client = create();
		client.create().forPath("/netty");
	}
}
