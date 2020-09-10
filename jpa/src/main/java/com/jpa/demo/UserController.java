package com.jpa.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpa.demo.dsl.QUser;
import com.jpa.demo.param.ResponseVO;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * @author Lee HN
 * @date 2019/5/18 21:45
 */
@RestController
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    private JPAQueryFactory queryFactory;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/user")
    public List<User> query(String name, Integer age, Long id) {
        return userRepository.findByNameOrAgeOrId(name, age, id);
    }

    @PostMapping("/and")
    public List<User> and(String name, Integer age, Long id) {
        return userRepository.findByNameAndAgeAndId(name, age, id);
    }

    public List<User> dsl(String name, Integer age, Long id) {
        QUser qUser = QUser.user;
        List<Tuple> fetch = queryFactory.select(qUser.name, qUser.sex).from(qUser)
                .where(qUser.name.eq(name).and(qUser.age.gt(age)).or(qUser.name.startsWith(name).and(qUser.age.lt(age)))).fetch();
        for (Tuple tuple : fetch) {
            String uname = tuple.get(qUser.name);
            Integer integer = tuple.get(qUser.sex);
        }
        return (List<User>) userRepository.findAll(qUser.name.eq(name).and(qUser.age.gt(age)).or(qUser.name.startsWith(name).and(qUser.age.lt(age))));
    }

    @GetMapping("/res")
    public ResponseVO res() throws JsonProcessingException {
        User user = userRepository.findById(1L).orElse(null);
        String s = objectMapper.writeValueAsString(user);
        return ResponseVO.success("{\"name\":\"hehe\"}", true);
    }

}
