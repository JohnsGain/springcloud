package com.shulian.ribbon.servicce.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.shulian.ribbon.entity.User;
import com.shulian.ribbon.servicce.IComputeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author zhangjuwa
 * @description 消费服务是吸纳
 * @date 2018/4/18
 * @since jdk1.8
 */
@Service
public class ComputeServiceImpl implements IComputeService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    @HystrixCommand(fallbackMethod = "addServiceFallBack")
    public User getUser(Integer id) {
        return restTemplate.getForObject("http://useradmin/user/{id}", User.class, id);
    }

    /**
     *  斷路之後的回調方法,回調方法參數和返回值要和服務請求方法參數和返回值一致
     * @param id
     * @return
     */
    public User addServiceFallBack(Integer id) {
        User user = new User();
        user.setName("error! this is circuit breaker.");
        user.setId(id);
        return user;
    }
}
