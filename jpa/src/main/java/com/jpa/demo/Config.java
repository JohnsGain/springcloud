package com.jpa.demo;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.querydsl.SimpleEntityPathResolver;

import javax.persistence.EntityManager;

/**
 * @author Lee HN
 * @date 2019/5/18 22:17
 */
@Configuration
public class Config {

    // 自定义查询dsl文件位置
    @Bean
    public EntityPathResolver SimpleEntityPathResolver() {
        return new SimpleEntityPathResolver(".dsl");
    }

    // dsl查询工厂
    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }
}
