package com.eureka.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 用于测试sleuth
 * @author ""
 * @Description:
 * @date 2018/5/20
 * @since jdk1.8
 */
@FeignClient("feign-consumer")
public interface ISleuthFeign {

    @GetMapping("/info")
    String getInfo();
}
