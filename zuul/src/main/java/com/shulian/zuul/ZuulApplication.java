package com.shulian.zuul;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 *   SpringCloud 是基于springboot的一整套实现微服务的框架。SpringCloud 包含了非常多的子框架，
 *   其中springcloud netflix就是其中一套框架，由netflix开发后来又并入springcloud 大家庭。
 Netflix OSS（Open Source）就是由Netflix公司主持开发的一套代码框架和库，目的是解决上了规模之后的分布式系统可能出现的一些有趣问题
 * @author zhangjuwa
 * @description Spring Cloud Netflix Zuul服务网关 ：网关服务是所有外部访问的统一入口，他
 * 除了具有服务路由，负载均衡功能外，还把权限控制从具体的业务服务中抽离，统一管理，
 * 让业务服务具有更高的复用性，可测试性。
 * @date 2018/4/19
 * @since jdk1.8
 */
@EnableZuulProxy  //启用zuul网关代理
@SpringBootApplication
public class ZuulApplication {

    public static void main(String[] args) {
            new SpringApplicationBuilder(ZuulApplication.class).web(true).run(args);
    }

}
