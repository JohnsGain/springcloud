package com.shulian.ribbon.controllre;

import com.shulian.ribbon.entity.User;
import com.shulian.ribbon.servicce.IComputeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * @author zhangjuwa
 * @description 服務消費者控制器
 * @date 2018/4/17
 * @since jdk1.8
 */
@RestController
public class ConsumerController {
    private final Logger logger = LoggerFactory.getLogger(ConsumerController.class);
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private IComputeService computeService;

    /**
     * @return
     */
    @GetMapping(value = "/user/{id}")
    public User add(@PathVariable Integer id) {
        logger.info("id is : "+ id);
        return computeService.getUser(id);
    }
    /**
     * @return
     */
    @GetMapping(value = "/hello/{id}")
    public String hello(@PathVariable Integer id) {
        logger.info("id is : "+ id);

        return restTemplate.getForEntity("http://useradmin/user/hello" , String.class).getBody();
    }
}
