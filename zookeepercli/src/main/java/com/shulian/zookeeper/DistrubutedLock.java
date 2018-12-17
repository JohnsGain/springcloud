package com.shulian.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author zhangjuwa
 * @description 基于zk的分布式锁实现
 * @date 2018/9/17
 * @since jdk1.8
 */
public class DistrubutedLock {

    static Logger logger = LoggerFactory.getLogger(DistrubutedLock.class);

    public static void main(String[] args) throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        //创建客户端
        CuratorFramework framework = CuratorFrameworkFactory.newClient("192.168.2.111:2181", 60000, 3000, retryPolicy);
        framework.start();
        //创建分布式锁, 锁空间的根节点路径为/curator/lock
        InterProcessMutex mutex = new InterProcessMutex(framework, "/curator/lock2");
        if (mutex.acquire(1000, TimeUnit.MICROSECONDS)) {
            try {
                ///获得了锁, 进行业务流程
                logger.info("获得了锁, 进行业务流程.....");
                //创建节点
                //String path = framework.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                //        .forPath("/my/path", "测试数据".getBytes(Charset.defaultCharset()));
                //logger.info("子节点 ：" + path);
            } finally {
                //完成业务流程, 释放锁
                mutex.release();
            }
        }
        //关闭客户端
        framework.close();
    }

}
