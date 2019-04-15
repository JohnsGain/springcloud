package com.shulian.ribbon;

import org.omg.CORBA.SetOverrideType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author ""
 * @date 2018/4/17
 * @description 使用Ribbon来调用服务，并实现客户端的均衡负载的消费者
 * Ribbon可以在通过客户端中配置的ribbonServerList服务端列表去轮询访问以达到均衡负载的作用。
 * @since jdk1.8
 */
@EnableDiscoveryClient
@EnableHystrixDashboard //启用Hystrix-dashboard断路器监控仪表盘页面
@EnableHystrix //启用断路器，里面包含了@EnableCircuitBreaker
@SpringBootApplication
public class RibbonApplication {

    public static void main(String[] args) {
        SpringApplication.run(RibbonApplication.class, args);
    }

    /**
     * @LoadBalanced 注解开启均衡负载能力
     * @return
     *
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
