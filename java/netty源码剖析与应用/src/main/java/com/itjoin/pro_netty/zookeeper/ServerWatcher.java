package com.itjoin.pro_netty.zookeeper;
import com.itjoin.pro_netty.constant.Constants;
import com.itjoin.pro_netty.server.NettyServer;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 服务端与zk连接的监听类，
 * 以防session断了，导致服务端注册到zk的临时节点丢失
 */
public class ServerWatcher implements CuratorWatcher {
    public static String serverKey = "";
    public static ServerWatcher serverWatcher = null;

    public static ServerWatcher getInstance() {
        if (serverWatcher == null) {
            serverWatcher = new ServerWatcher();
        }
        return serverWatcher;
    }

    /**
     * 监听zk路径的变化,
     * 只要有服务器session断了，就会触发，
     * 当本机与zk的session断了，需要重新创建临时节点
     *
     * @param event
     * @throws Exception
     */
    @Override
    public void process(WatchedEvent event) throws Exception {
        System.out.println("========服务器监听zk=event==="+event.getState()+"==="
                +new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        //当会话丢失
        if (event.getState().equals(Watcher.Event.KeeperState.Disconnected)
                ||event.getState().equals(Watcher.Event.KeeperState.Expired)) {
            try {
                try {
                    //先尝试去关闭旧的连接
                    ZookeeperFactory.create().close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                CuratorFramework client = ZookeeperFactory.recreate();
                client.getChildren().usingWatcher(this).forPath(NettyServer.SERVER_PATH);
                //获取当前服务器ip
                InetAddress netAddress = InetAddress.getLocalHost();
                Stat stat = client.checkExists().forPath(NettyServer.SERVER_PATH);
                if (stat == null) {
                    client.create().creatingParentsIfNeeded()
                            .withMode(CreateMode.PERSISTENT).forPath(NettyServer.SERVER_PATH, "0".getBytes());
                }
                //在SERVER_PATH目录下构建临时节点，如10.118.15.15#8080#1#0000000000
                client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).
                        forPath(NettyServer.SERVER_PATH + "/" + netAddress.getHostAddress() + "#" + Constants.port + "#" + Constants.weight + "#");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            //其他事件发生时，只需要设置监听即可
            CuratorFramework client = ZookeeperFactory.create();
            client.getChildren().usingWatcher(this).forPath(NettyServer.SERVER_PATH);
        }
    }
}
