package com.shulian.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhangjuwa
 * @description curator分布式读写锁
 * @date 2018/9/18
 * @since jdk1.8
 */
public class CuratorDistributedReadWriteLock {

    static Logger logger = LoggerFactory.getLogger(CuratorDistributedReadWriteLock.class);


    public static void main(String[] args) throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.2.111:2181", retryPolicy);
        //声明读写锁对象
        InterProcessReadWriteLock mutex = new InterProcessReadWriteLock(client, "/myreadwritelock");
        //获取读锁对象
        InterProcessMutex readLock = mutex.readLock();
        //尝试获取锁
        readLock.acquire();
        try {
            logger.info("执行读任务，当另外一个线程也在执行一个读锁任务时，这两个请求不会互斥");
        } finally {
            readLock.release();
        }

    }
}
