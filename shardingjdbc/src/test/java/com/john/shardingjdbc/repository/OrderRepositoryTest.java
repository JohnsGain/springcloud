package com.john.shardingjdbc.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.john.shardingjdbc.BaseTest;
import com.john.shardingjdbc.domain.entity.OrderEntity;
import com.john.shardingjdbc.domain.repository.OrderRepository;
import com.john.shardingjdbc.util.JsonUtils;
import org.apache.shardingsphere.infra.hint.HintManager;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * 不分表的数据库
 *
 * @author zhangjuwa  <a href="mailto:zhangjuwa@gmail.com">zhangjuwa</a>
 * @date 2023/8/7 22:52
 * @since jdk1.8
 */
public class OrderRepositoryTest extends BaseTest {

    @Resource
    private OrderRepository orderRepository;

    @Test
    public void hint() {
        HintManager hintManager = HintManager.getInstance();
//        hintManager.addDatabaseShardingValue();
    }

    @Test
    public void selectList() {
        LambdaQueryWrapper<OrderEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(OrderEntity::getId, OrderEntity::getPlatformOrderNo);
        wrapper.eq(OrderEntity::getPlatformOrderNo, "333");
        List<OrderEntity> selectList = orderRepository.selectList(wrapper);
        System.out.println(JsonUtils.toString(selectList));
    }

    @Test
    public void selectById() {
        OrderEntity entity = orderRepository.selectById(895475209836953601L);
        System.out.println(entity);
        OrderEntity entity1 = orderRepository.selectById(895475209887285248L);
        System.out.println(entity1);
    }

    @Test
    public void insert() {
        OrderEntity entity = new OrderEntity();
        entity.setPlatformOrderNo("555");
        entity.setSource("SY");
        entity.setPlatformStatus(1);
        for (int i = 0; i < 5; i++) {
            entity.setOrderNo("order_no" + i);
            entity.setRemark("x" + i);
            orderRepository.insert(entity);

        }
    }

}
