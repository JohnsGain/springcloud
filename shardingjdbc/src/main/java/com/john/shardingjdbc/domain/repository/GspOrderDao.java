package com.john.shardingjdbc.domain.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.john.shardingjdbc.domain.entity.GspOrderEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * gsp订单 dao
 *
 * @author zhangjuwa zhangjuwa@gongpin.com
 * @since 1.0.0 2022-07-18
 */
@Mapper
public interface GspOrderDao extends BaseMapper<GspOrderEntity> {

}
