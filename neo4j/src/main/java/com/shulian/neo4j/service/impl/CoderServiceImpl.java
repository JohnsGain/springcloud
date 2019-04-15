package com.shulian.neo4j.service.impl;

import com.shulian.neo4j.domain.entity.Coder;
import com.shulian.neo4j.domain.repository.CoderRepository;
import com.shulian.neo4j.service.ICoderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @author ""
 * @description Coder服务层实现
 * @date 2018/9/5
 * @since jdk1.8
 */
@Service
public class CoderServiceImpl implements ICoderService {

    @Autowired
    private CoderRepository coderRepository;

    @Override
    public Coder get(Long id) {
        return coderRepository.findOne(id);
    }

    @Override
    public boolean delete(Long id) {
        try {
            coderRepository.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Coder update(Coder coder) {
        boolean exists = coderRepository.exists(coder.getId());
        if (exists) {
            coderRepository.save(coder);
        }
        return coder;
    }

    @Override
    @Transactional
    public Coder add(Coder coder) {
        Coder save = coderRepository.save(coder);
        if (save.getName().equals("john")) {
            throw new RuntimeException("12345");
        }
        return save;
    }

    @Override
    public List<Coder> findByName(String name) {
        return coderRepository.findByName(name);
    }

    @Override
    public List<Coder> findAll() {
        return (List<Coder>) coderRepository.findAll();
    }

    @Override
    public Set<Coder> listByIds(List<Long> ids) {
        return (Set<Coder>) coderRepository.findAll(ids);
    }
}
