package com.john.shardingjdbc.controller;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("test")
public class TestController {

    @Resource
    private Environment environment;

    @GetMapping("get")
    public String get(@RequestParam("param") String param) {
        Integer port = environment.getProperty("server.port", Integer.class);
        String orderType = environment.getProperty("order.type", String.class);
        return param + port + orderType;
    }
}
