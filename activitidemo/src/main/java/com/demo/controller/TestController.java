package com.demo.controller;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ""
 * @date 2019/3/15
 * @since jdk1.8
 **/
@RestController
public class TestController {

    @Autowired
    private RuntimeService runtimeService;

    @GetMapping("get")
    public String get() {
        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
        List<ProcessInstance> list = processInstanceQuery.list();
        return "helloworld" + list.size();
    }
}
