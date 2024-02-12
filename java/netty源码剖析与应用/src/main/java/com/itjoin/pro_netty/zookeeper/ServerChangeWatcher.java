package com.itjoin.pro_netty.zookeeper;
import java.util.ArrayList;
import java.util.List;
import com.itjoin.pro_netty.client.ChannelFutureManager;
import com.itjoin.pro_netty.client.NettyClient;
import com.itjoin.pro_netty.constant.Constants;
import com.itjoin.pro_netty.server.NettyServer;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;
import io.netty.channel.ChannelFuture;
import org.apache.zookeeper.Watcher;

public class ServerChangeWatcher implements CuratorWatcher {
    public static ServerChangeWatcher serverChangeWatcher = null;
    //客户端与服务器的连接个数，需要放入配置文件中方便调整
    public static final int SERVER_COUNT=1000;

    public static ServerChangeWatcher getInstance(){
        if(serverChangeWatcher==null){
            serverChangeWatcher=new ServerChangeWatcher();
        }
        return  serverChangeWatcher;
    }
	/**
	 * 监听zk路径的变化,
	 * 只要有事件发生，则需重新获取所有服务器列表，
	 * 并更新连接列表
	 * @param event
	 * @throws Exception
	 */
	@Override
	public synchronized  void process(WatchedEvent event) throws Exception {
		System.out.println("==========重新初始化服务器连接process======");
		//假如是与zk连接断了，需要重连
        if (event.getState().equals(Watcher.Event.KeeperState.Disconnected)
				//连接过期，client时效
				|| event.getState().equals(Watcher.Event.KeeperState.Expired)) {
                CuratorFramework client = ZookeeperFactory.recreate();
                client.getChildren().usingWatcher(this).forPath(NettyServer.SERVER_PATH);
			return ;
        }else if(event.getState().//重新连上，并未过期
				equals(Watcher.Event.KeeperState.SyncConnected)
		            && !event.equals(Watcher.Event.EventType.NodeChildrenChanged)){
			CuratorFramework client = ZookeeperFactory.create();
			client.getChildren().usingWatcher(this).forPath(NettyServer.SERVER_PATH);
			return ;
		}
        //假如是有服务器发生了变化
        System.out.println("zookeeper==state==="+event.getState());
		 CuratorFramework client = ZookeeperFactory.create();
		 client.getChildren().usingWatcher(this).forPath(NettyServer.SERVER_PATH);
		 List<String> serverPaths = client.getChildren().forPath(NettyServer.SERVER_PATH);
		 List<String> servers = new ArrayList<>();
		 //获取服务器列表，并交给ChannelFutureManager保存
		 for(String serverPath : serverPaths){
		     System.out.println("===服务器链表=="+serverPath);
			 String[] str = serverPath.split("#");
			 int weight = Integer.valueOf(str[2]);
			    //不同server，其权重值可能不同
			    //此处权重做了简单处理，为1时构建SERVER_COUNT个连接，2翻倍，依此类崔
				if(weight>0){
					for(int w=0;w<=weight*SERVER_COUNT;w++){
						servers.add(str[0]+"#"+str[1]);
					}
				}
		 }
		 if(servers.size()>0){
			 ChannelFutureManager.serverList.clear();
			 ChannelFutureManager.serverList.addAll(servers);

			 //根据服务器地址和ip，构建连接，
			 //并交给ChannelFutureManager保存
			 List<ChannelFuture> futures = new ArrayList<>();
			 for(String realServer : ChannelFutureManager.serverList){
				 String[] str = realServer.split("#");
				 try {
					 //此处NettyClient的bootstrap不能静态化
					 ChannelFuture  channelFuture = NettyClient.getBootstrap()
							 .connect(str[0], Integer.valueOf(str[1])).sync();
					 futures.add(channelFuture);;
				 } catch (Exception e) {
					 e.printStackTrace();
				 }
			 }

			 //加上锁,防止获取不到ChannelFuture
			 synchronized (ChannelFutureManager.position){
				 //先清空ChannelFuture列表
				 ChannelFutureManager.clear();
				 ChannelFutureManager.addAll(futures);
			 }
		 }
	}

	/**
	 * 初始化服务器连接列表
	 * @throws Exception
	 */
	public  static void initChannelFuture() throws Exception {
		CuratorFramework client = ZookeeperFactory.create();
		List<String> servers = client.getChildren().forPath(Constants.SERVER_PATH);
		System.out.println("==========初始化服务器连接======");
		for(String server : servers){
			String[] str = server.split("#");
			try {
				int weight = Integer.valueOf(str[2]);
				if(weight>=0){
					for(int w=0;w<=weight*SERVER_COUNT;w++){
						ChannelFuture  channelFuture = NettyClient.
								getBootstrap().connect(str[0], Integer.valueOf(str[1])).sync();
						ChannelFutureManager.add(channelFuture);;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
        client.getChildren().usingWatcher(getInstance()).forPath(Constants.SERVER_PATH);
	}
	public static void main(String[] args) throws Exception {
		initChannelFuture();
	}
}
