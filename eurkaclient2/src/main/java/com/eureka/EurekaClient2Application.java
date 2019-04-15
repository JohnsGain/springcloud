package com.eureka;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;

/**
 * @author ""
 * @Description:
 * @date 2018/5/20
 * @since jdk1.8
 */
@EnableDiscoveryClient
//启用feign功能
@EnableFeignClients
@SpringBootApplication
@EnableHystrixDashboard    //Z启用Hystrix-dashboard仪表盘
@MapperScan("com.eureka.repository")
public class EurekaClient2Application {
    public static void main(String[] args) {
        SpringApplication.run(EurekaClient2Application.class, args);
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
