package com.eureka.controller;

import com.eureka.config.EnviMyWebAppConfigurer;
import com.eureka.entity.User;
import com.eureka.service.ISleuthFeign;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangjuwa
 * @date 2018/4/17
 * @description 用户控制器
 * @since jdk1.8
 */
@RestController
@RequestMapping("/user")
@RefreshScope
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private EnviMyWebAppConfigurer webAppConfigurer;

    @Autowired
    private ISleuthFeign sleuthFeign;

    @GetMapping("/{id}")
    public User get(@PathVariable("id") Integer id) {
        User user = new User();
        user.setAge(23);
        user.setId(id);
        String info = sleuthFeign.getInfo();
        user.setName("john" + webAppConfigurer.getEnv().getProperty("server.port") + info);
        logger.info("获取用户 ：" + user);
        return user;
    }

    @RequestMapping("/hi")
    public String home(){
        return "hi i'm useradmin2-miya!";
    }

    @Value("${year}")
    private String year;

    @GetMapping("/year")
    @ApiOperation("测试动态刷新配置")
    public String year() {
        return "hello-world " + year;
    }
}
