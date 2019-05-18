package com.jpa.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * @author Lee HN
 * @date 2019/5/18 21:46
 */
public interface UserRepository extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User> {

    List<User> findByNameOrAgeOrId(String name, Integer age, Long id);

    @Nullable
    List<User> findByNameAndAgeAndId(String name, @Nullable Integer age, Long id);
}
