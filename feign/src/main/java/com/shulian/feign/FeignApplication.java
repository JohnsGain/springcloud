package com.shulian.feign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;

/**
 * Spring Cloud为Feign增加了对Spring MVC注解的支持，还整合了Ribbon和Eureka来提供均衡负载的HTTP客户端实现。
 * Feign自帶了斷路器功能，默認是禁用的
 * @author zhangjuwa
 * @description feign负载均衡器
 * @date 2018/4/17
 * @since jdk1.8
 */
@EnableDiscoveryClient
//启用feign功能
@EnableFeignClients
@SpringBootApplication
@EnableHystrixDashboard    //Z启用Hystrix-dashboard仪表盘
public class FeignApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeignApplication.class, args);
    }

    /**
     * 使用sleuth追踪的时候添加的配置
     * @return
     */
    @Bean
    public AlwaysSampler defaultSampler(){
        return new AlwaysSampler();
    }
}
