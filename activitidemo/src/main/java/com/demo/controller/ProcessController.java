package com.demo.controller;

import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangjuwa
 * @date 2019/5/17
 * @since jdk1.8
 */
@RestController
@RequestMapping("process")
public class ProcessController {

    @Autowired
    private RuntimeService runtimeService;

}
