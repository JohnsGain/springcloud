package eureka.controller;

import eureka.config.EnviMyWebAppConfigurer;
import eureka.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.nio.cs.US_ASCII;

import java.util.List;

/**
 * @author ""
 * @date 2018/4/17
 * @description 用户控制器
 * @since jdk1.8
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private DiscoveryClient client;

    @Autowired
    private EnviMyWebAppConfigurer webAppConfigurer;

    @GetMapping("/{id}")
    public User get(@PathVariable Integer id) {
        //获取在eureka注册的当前服务实例清单
        List<String> services = client.getServices();
        services.stream().forEach(s -> {
            System.out.println(s);
        });
        User user = new User();
        user.setAge(12);
        user.setId(id);
        user.setName("john" + webAppConfigurer.getEnv().getProperty("server.port"));
        logger.info("获取用户 ：" + user);
        return user;
    }

    @GetMapping("/hello")
    public String hello() {
        User user = new User();
        user.setAge(12);
        user.setId(2);
        user.setName("john");
        logger.info("获取用户 ：" + user);
        return "hello world!";
    }

}
