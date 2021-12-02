package com.czxy.zookeeper.demo01;

import org.apache.zookeeper.*;

import java.io.IOException;

/**
 * @author 550894211@qq.com
 * @version v 1.0
 * @date 2019/12/9
 */
public class ZookeeperDemo{
    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        //初始化 Zookeeper 实例 (zk地址,会话超时时间,与系统默认一致,watcher)
        ZooKeeper zk = new ZooKeeper("192.168.100.109:2181,192.168.100.110:2181,192.168.100.111:2181", 30000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println(watchedEvent.getType());
                System.out.println(watchedEvent.getPath());
                System.out.println(watchedEvent.getState());
            }
        });
        //创建一个子节点
        String s = zk.create("/test03", "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }
}
