package com.eureka.controller;

import com.eureka.repository.OrderRepository;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;

/**
 * @author ""
 * @description
 * @date 2018/9/17
 * @since jdk1.8
 */
@RestController
public class OrderController {
    static Logger logger = LoggerFactory.getLogger(OrderController.class);


    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CuratorFramework curatorFramework;

    final CountDownLatch downLatch = new CountDownLatch(1);

    @GetMapping("order")
    //@Transactional(rollbackFor = Exception.class)
    public Object order() {
        try {
            InterProcessMutex mutex = new InterProcessMutex(curatorFramework, "/curator/lock");
            mutex.acquire();
            try {
                logger.info("2 ==获得了锁, 进行业务流程.....");
                Integer byId = orderRepository.findById(1);
                orderRepository.decreaseOrder(1, byId - 2);
            } finally {
                mutex.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "success";
    }
}
