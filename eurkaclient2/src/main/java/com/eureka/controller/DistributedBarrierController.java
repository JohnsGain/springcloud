package com.eureka.controller;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangjuwa
 * @description curator分布式同步器
 * @see {http://blog.51cto.com/fanfusuzi/1936684}
 * 分布式Barrier 类{@link DistributedDoubleBarrier}：
 * @date 2018/9/18
 * @since jdk1.8
 */
@RestController
public class DistributedBarrierController {

    static Logger logger = LoggerFactory.getLogger(DistributedBarrierController.class);

    @Autowired
    private CuratorFramework curatorFramework;

    /**
     * 它会阻塞所有节点上的等待进程，直到某一个被满足，
     *  * 然后所有的节点同时开始，中间谁先运行完毕，谁后运行完毕不关心，但是最终一定是一块退出运行的
     * @return
     * @throws Exception
     */
    @GetMapping("barrier")
    public String barrier() throws Exception {
        //设置阈值为5，改barrier会阻塞5个客户端线程，超过5个线程不再阻塞，开始进入执行
        DistributedDoubleBarrier barrier = new DistributedDoubleBarrier(curatorFramework, "/curator/barrier", 2);
        barrier.enter();
        try {
            logger.info("同时进入执行");
        } finally {
            barrier.leave();
            logger.info("同时离开");
        }
        return null;
    }

    /**
     * 它会阻塞所有节点上的等待进程（所有节点进入待执行状态），直到“阻塞的这个节点被删除(改节点更新也不行)， 然后所有的节点同时开始
     * @return
     */
    @GetMapping("barrier2")
    public String barrier2() throws Exception {
        DistributedBarrier barrier = new DistributedBarrier(curatorFramework, "/curator/barrier2");
        barrier.setBarrier();
        barrier.waitOnBarrier();
        try {
            logger.info("进入执行");
        } finally {
            barrier.removeBarrier();
            logger.info("释放");
        }
        return "success";
    }
}
