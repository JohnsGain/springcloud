package com.shulian.neo4j.domain.repository;

import com.shulian.neo4j.domain.entity.Coder;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhangjuwa
 * @description coder仓储
 * @date 2018/9/5
 * @since jdk1.8
 */
@Repository
public interface CoderRepository extends GraphRepository<Coder> {

    /*
	 CoderRepositiory 继承 GraphRepository类，实现增删查改
	 实现自己的接口：通过名字查询Coder（可以是单个节点，也可以是一组节点List集合）
	 spring-data-neo4j 支持方法命名约定查询 findBy{Coder的属性名}
	 findBy后面的属性名一定要在Coder节点实体类里存在，否则会报错
	 */

    List<Coder> findByName(@Param("name") String name);

}
