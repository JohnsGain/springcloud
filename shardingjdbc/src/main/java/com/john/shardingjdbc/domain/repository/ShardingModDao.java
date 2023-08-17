package com.john.shardingjdbc.domain.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.john.shardingjdbc.domain.entity.ShardingModEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author zhangjuwa  <a href="mailto:zhangjuwa@gmail.com">zhangjuwa</a>
 * @date 2023/8/17 21:50
 * @since jdk1.8
 */
@Mapper
public interface ShardingModDao extends BaseMapper<ShardingModEntity> {

    @Select("select * FROM sharding_mod WHERE id between #{min} and #{max} ")
    List<ShardingModEntity> selectByIdRange(@Param("min") Long min, @Param("max") Long max);
}
