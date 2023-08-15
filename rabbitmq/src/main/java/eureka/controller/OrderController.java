package eureka.controller;

import eureka.repository.OrderRepository;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 *  * @see {http://blog.51cto.com/fanfusuzi/1936684}
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

    final CountDownLatch countDown = new CountDownLatch(1);

    @GetMapping("order")
    //@Transactional(rollbackFor = Exception.class)
    public Object order() throws InterruptedException {
        try {
            InterProcessMutex mutex = new InterProcessMutex(curatorFramework, "/curator/lock");
            mutex.acquire();
            logger.info("1 ==获得了锁, 进行业务流程.....");
            Thread.sleep(15000);
            try {
                Integer byId = orderRepository.findById(1);
                orderRepository.decreaseOrder(1, byId - 1);
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
