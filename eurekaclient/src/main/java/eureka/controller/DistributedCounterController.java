package eureka.controller;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.retry.RetryNTimes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  * @see {http://blog.51cto.com/fanfusuzi/1936684}
 * @author ""
 * @description curator分布式计数器
 * @date 2018/9/18
 * @since jdk1.8
 */
@RestController
public class DistributedCounterController {

    static Logger logger = LoggerFactory.getLogger(DistributedCounterController.class);

    @Autowired
    private CuratorFramework curatorFramework;

    /**
     * 分布式环境下的数值原子递增递减操作
     *
     * @return
     */
    @GetMapping("count")
    public Integer count() throws Exception {
        //这里是不同的重试策略实现
        //RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        //重试3次，每次重试之前睡眠一秒
        RetryPolicy retryPolicy = new RetryNTimes(3, 1000);
        DistributedAtomicInteger atomicInteger = new DistributedAtomicInteger(curatorFramework, "/curator/atomic",
                retryPolicy);
        AtomicValue<Integer> value = atomicInteger.increment();
        logger.info("修改前的值:{}", value.preValue());
        logger.info("修改后的值:{}", value.postValue());
        return value.postValue();
    }

    /**
     * 通过这个接口初始化计数器原始值，默认原始值是 0
     * @return
     * @throws Exception
     */
    @GetMapping("setcount")
    public Integer setCount() throws Exception {
        RetryPolicy retryPolicy = new RetryNTimes(3, 1000);
        DistributedAtomicInteger atomicInteger = new DistributedAtomicInteger(curatorFramework, "/curator/atomic",
                retryPolicy);
        AtomicValue<Integer> value = atomicInteger.get();
        logger.info("修改前的值:{}", value.preValue());
        logger.info("修改后的值:{}", value.postValue());
        atomicInteger.forceSet(10);
        Integer integer = atomicInteger.get().postValue();
        logger.info("初始化后的值:{}", integer);
        return integer;
    }
}
