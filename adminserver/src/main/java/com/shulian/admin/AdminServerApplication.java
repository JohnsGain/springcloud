package com.shulian.admin;

import de.codecentric.boot.admin.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author ""
 * @description springboot-admin应用监控组件
 * 通过HTTP或者使用 Eureka注册到admin server中进行展示，Spring Boot Admin UI部分使用AngularJs将数据展示在前端。
 *
 * Spring Boot Admin 是一个针对spring-boot的actuator接口进行UI美化封装的监控工具。
 *  他可以：在列表中浏览所有被监控spring-boot项目的基本信息，详细的Health信息、内存信息、JVM信息、垃圾回收信息、
 *  各种配置信息（比如数据源、缓存列表和命中率）等，还可以直接修改logger的level。
 * @date 2018/9/5
 * @since jdk1.8
 */
@SpringBootApplication
@EnableAdminServer
@EnableAutoConfiguration
@EnableDiscoveryClient
public class AdminServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminServerApplication.class, args);
    }
}
