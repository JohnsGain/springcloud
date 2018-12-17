package com.shulian.feign.service;

import com.shulian.feign.entity.User;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 用于测试sleuth追踪
 * @author zhangjuwa
 * @Description:
 * @date 2018/5/20
 * @since jdk1.8
 */
@FeignClient("useradmin2")
public interface IClient2Feign {
    /**
     * 参数名称相同也要在@PathVariable里面注明，否则报错
     * @param id
     * @return
     */
    @GetMapping("/user/{id}")
    User get(@PathVariable("id") Integer id);
}
