package com.shulian.feign.service.impl;

import com.shulian.feign.entity.User;
import com.shulian.feign.service.IUserClientFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

/** 服務短路之後,執行的回調方法。通过@FeigenClient(fallback = UserClientFeignHystrixImpl.class)制定,和调用的接口实现同一个接口
 * @author zhangjuwa
 * @description
 * @date 2018/4/18
 * @since jdk1.8
 */
@Component
public class UserClientFeignHystrixImpl implements IUserClientFeign {

    @Override
    public User get(@PathVariable("id") Integer id) {
        User user = new User();
        user.setId(id);
        user.setName("feign fallbacker circuit breaker ");
        return user;
    }
}
