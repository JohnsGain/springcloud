package com.john.shardingjdbc.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.john.shardingjdbc.BaseTest;
import com.john.shardingjdbc.domain.entity.GspOrderEntity;
import com.john.shardingjdbc.domain.repository.GspOrderDao;
import com.john.shardingjdbc.util.JsonUtils;
import org.apache.shardingsphere.infra.hint.HintManager;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhangjuwa  <a href="mailto:zhangjuwa@gmail.com">zhangjuwa</a>
 * @date 2023/8/7 22:52
 * @since jdk1.8
 */
public class GspOrderDaoTest extends BaseTest {

    @Resource
    private GspOrderDao gspOrderDao;

    /**
     * hint
     * 基于当前线程的业务逻辑进行数据分片，不依赖与表字段的方式分片
     * https://shardingsphere.apache.org/document/legacy/4.x/document/cn/manual/sharding-jdbc/usage/hint/
     */
    @Test
    public void hint() {
        HintManager hintManager = HintManager.getInstance();
//        hintManager.addTableShardingValue("gsp_order",);
    }


    @Test
    public void selectList() {
        LambdaQueryWrapper<GspOrderEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(GspOrderEntity::getId, GspOrderEntity::getPurchaseGroupName);
        wrapper.eq(GspOrderEntity::getPurchaseGroupName, "666");
        List<GspOrderEntity> selectList = gspOrderDao.selectList(wrapper);
        System.out.println(JsonUtils.toString(selectList));
    }

    @Test
    public void selectById() {
//        GspOrderEntity entity = gspOrderDao.selectById(895475209836953601L);
//        System.out.println(entity);
        List<GspOrderEntity> entity1 = gspOrderDao.selectBatchIds(
                Lists.newArrayList(895475209887285248L, 899053649433460737L));
        System.out.println(JsonUtils.toString(entity1));
    }

    @Test
    public void insert() {
//        GspOrderEntity entity=gspOrderDao.selectById()
        GspOrderEntity entity = new GspOrderEntity();
        entity.setPurchaseGroupName("666");
        entity.setConfirmStatus(1);
        entity.setVersion(1);
        for (int i = 0; i < 5; i++) {
            entity.setGspOrderNo("order_no" + i + 10);
            entity.setGspConfirmStatus(i);
            gspOrderDao.insert(entity);

        }
    }

}
