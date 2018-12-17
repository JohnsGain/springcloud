package com.shulian.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangjuwa
 * @date 2018/4/17
 * @description eureka服务
 * @since jdk1.8
 */
@EnableEurekaServer
@SpringBootApplication
@RestController
public class EurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }

    @GetMapping("test")
    public String get() {
        return "hello world";
    }

}


