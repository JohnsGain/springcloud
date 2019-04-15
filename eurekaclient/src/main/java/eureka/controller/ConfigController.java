package eureka.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ""
 * @description 配置控制器，从配置中心读取配置
 * @RefreshScope 实现动态刷新配置，需要导入spring-boot-starter-acturator,
 * 通过调用/refresh的Post请求刷新配置，不用重启服务
 * @date 2018/4/19
 * @since jdk1.8
 */
@RestController
@RefreshScope
public class ConfigController {

    /**
     * 从配置中心获取配值装配
     */
    @Value("${username2}")
    private String username;

    /**
     * @return  当前应用名称
     */
    @GetMapping("/name")
    public String getApplicationName() {
        return username;
    }
}
