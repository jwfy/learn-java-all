package com.jwfy.learn.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;

public class ZkMain {

    private String host;
    private int port;
    private int sessionTimeout;
    private CuratorFramework client;

    public ZkMain(String host, int port, int sessionTimeout) {
        this.host = host;
        this.port = port;
        this.sessionTimeout = sessionTimeout;

        RetryPolicy policy = new ExponentialBackoffRetry(1000, 3);

        this.client = CuratorFrameworkFactory
                .builder()
                .connectString(host)
                .sessionTimeoutMs(sessionTimeout)
                .retryPolicy(policy)
                .build();
        this.client.start();
    }

    public void cleanAllPath(String path) {
        try {
            Stat stat = new Stat();
            client.getData().storingStatIn(stat).forPath(path);
            this.client.delete().deletingChildrenIfNeeded().withVersion(stat.getVersion()).forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ZkMain zkMain = new ZkMain(ZkUtil.host, ZkUtil.port, ZkUtil.sessionTimeout);
        zkMain.cleanAllPath("/kafka/cluster");
    }




}
