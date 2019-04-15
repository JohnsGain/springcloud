package com.shulian.feign.controller;

import com.shulian.feign.entity.User;
import com.shulian.feign.service.IClient2Feign;
import com.shulian.feign.service.IUserClientFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.remote.JMXPrincipal;

/**
 * @author ""
 * @description 通过feign整合的负载均衡调用服务
 * @date 2018/4/17
 * @since jdk1.8
 */
@RestController
public class UserFeignController {

    @Autowired
    private IUserClientFeign userClientFeign;

    @Autowired
    private IClient2Feign client2Feign;

    @GetMapping("/feignuser/{id}")
    public User get(@PathVariable("id") Integer id) {
        return userClientFeign.get(id);
    }

    /**
     * 测试sleuth的追踪功能
     * @param id
     * @return
     */
    @GetMapping("/sleuth/{id}")
    public User sleuth(@PathVariable("id") Integer id) {
        return client2Feign.get(id);
    }

    /**
     * 让EurekaCleint2Application调用
     * @return
     */
    @GetMapping("/info")
    public String getInfo(){
        return "123455";
    }
}
