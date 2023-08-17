package com.john.shardingjdbc.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.john.shardingjdbc.BaseTest;
import com.john.shardingjdbc.domain.entity.ShardingModEntity;
import com.john.shardingjdbc.domain.repository.ShardingModDao;
import com.john.shardingjdbc.util.JsonUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.shardingsphere.infra.hint.HintManager;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhangjuwa  <a href="mailto:zhangjuwa@gmail.com">zhangjuwa</a>
 * @date 2023/8/7 22:52
 * @since jdk1.8
 */
public class ShardingModDaoTest extends BaseTest {

    @Resource
    private ShardingModDao shardingModDao;


    @Test
    public void selectList() {
        LambdaQueryWrapper<ShardingModEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(ShardingModEntity::getId, ShardingModEntity::getOrderNo);
        wrapper.eq(ShardingModEntity::getSource, "sy");
        List<ShardingModEntity> selectList = shardingModDao.selectList(wrapper);
        System.out.println(JsonUtils.toString(selectList));
    }

    @Test
    public void selectById() {
        List<ShardingModEntity> entity1 = shardingModDao.selectBatchIds(Lists.newArrayList(899053649433460737L,
                899053648808509440L, 895475209887285248L));
        System.out.println(JsonUtils.toString(entity1));
    }


    @Test
    public void selectByIdRange() {
        List<ShardingModEntity> entity1 = shardingModDao.selectByIdRange(899053648808509440L, 899053648808509442L);
        System.out.println(JsonUtils.toString(entity1));
    }

    @Test
    public void insert() {
        ShardingModEntity entity = new ShardingModEntity();
        entity.setSource("sy");
        entity.setDiscount(BigDecimal.ONE);
        entity.setPlatformStatus(1);
        for (int i = 0; i < 15; i++) {
            entity.setOrderNo("order_no" + i + 10);
            entity.setPlatformOrderNo(RandomStringUtils.random(8, "1234567890ASDFGHJ"));
            shardingModDao.insert(entity);
        }
    }

    @Test
    public void hint() {
// 清除掉上一次的规则，否则会报错
        HintManager.clear();
// HintManager API 工具类实例
        try (HintManager hintManager = HintManager.getInstance();) {
            //分库不分表情况下，强制路由至某一个分库时，可使用hintManager.setDatabaseShardingValue方式添加分片。
            // 通过此方式添加分片键值后，将跳过SQL解析和改写阶段，从而提高整体执行效率。
            //hintManager.setDatabaseShardingValue(1);
            // 直接指定对应具体的数据库
            hintManager.addDatabaseShardingValue("ds", 0);
// 设置表的分片健
            hintManager.addTableShardingValue("t_order", 0);
            hintManager.addTableShardingValue("t_order", 1);
            hintManager.addTableShardingValue("t_order", 2);

// 在读写分离数据库中，Hint 可以强制读主库
//        hintManager.setMasterRouteOnly();
//        hintManager.setWriteRouteOnly();
        }


    }

}
