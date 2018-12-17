package com.shulian.feign.service;

import com.shulian.feign.entity.User;
import feign.hystrix.FallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *  使用@FeignClient("useradmin")注解来绑定该接口对应useradmin服务，useradmin代表在eureka注册的服务名
 *  通过fallback指定服务短路之后的回调实现类
 * @author zhangjuwa
 * @since jdk1.8
 */
//@FeignClient(value = "useradmin", fallback = UserClientFeignHystrixImpl.class)//使用fallback的回退方式不能获取错误信息
@FeignClient(value = "useradmin", fallbackFactory = IUserClientFeign.HystrixClientFallbackFactoryImpl.class)   //这种回退方式可以访问导致回退触发器的原因
public interface IUserClientFeign {

    /**
     * 参数名称相同也要在@PathVariable里面注明，否则报错
     * @return
     */
    @GetMapping("/user/{id}")
    User get(@PathVariable("id") Integer id);

    @Component
    class HystrixClientFallbackFactoryImpl implements FallbackFactory<IUserClientFeign> {
        @Override
        public IUserClientFeign create(Throwable throwable) {
            return (s -> {
                User user = new User();
                user.setId(s);
                user.setName(throwable.getMessage());
                user.setAge(22);
                return user;
            });
        }

    }
}
