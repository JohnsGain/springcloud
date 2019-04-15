package com.shulian.neo4j.service;

import com.shulian.neo4j.domain.entity.Coder;

import java.util.List;
import java.util.Set;

/**
 * @author ""
 * @description Coder服务层接口
 * @date 2018/9/5
 * @since jdk1.8
 */
public interface ICoderService {

    Coder get(Long id);

    boolean delete(Long id);

    Coder update(Coder coder);

    Coder add(Coder coder);

    List<Coder> findByName(String name);

    List<Coder> findAll();

    Set<Coder> listByIds(List<Long> ids);
}
