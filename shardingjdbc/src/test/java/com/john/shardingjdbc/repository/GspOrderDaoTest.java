package com.john.shardingjdbc.repository;

import com.john.shardingjdbc.BaseTest;
import com.john.shardingjdbc.domain.entity.GspOrderEntity;
import com.john.shardingjdbc.domain.repository.GspOrderDao;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @author zhangjuwa  <a href="mailto:zhangjuwa@gmail.com">zhangjuwa</a>
 * @date 2023/8/7 22:52
 * @since jdk1.8
 */
public class GspOrderDaoTest extends BaseTest {

    @Resource
    private GspOrderDao gspOrderDao;

    @Test
    public void test() {
//        GspOrderEntity entity=gspOrderDao.selectById()
        GspOrderEntity entity = new GspOrderEntity();
        entity.setPurchaseGroupName("333");
        entity.setConfirmStatus(1);
        entity.setVersion(1);
        for (int i = 0; i < 5; i++) {
            entity.setGspOrderNo("order_no" + i);
            entity.setGspConfirmStatus(i);
            gspOrderDao.insert(entity);

        }
    }

}
