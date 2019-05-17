package com.demo.controller;

import com.alibaba.fastjson.JSON;
import com.demo.Result;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @GetMapping("get")
    public String get() {
        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
        List<ProcessInstance> list = processInstanceQuery.list();
        return "helloworld" + list.size();
    }

    @GetMapping("deploy")
    public Result<List<Deployment>> list() {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        for (ProcessDefinition processDefinition : list) {
            System.out.println(processDefinition);
        }
        List<Deployment> list1 = repositoryService.createDeploymentQuery()
                .list();
        for (Deployment deployment : list1) {
            System.out.println(deployment);
        }
        return new Result<>(null);
    }

    @GetMapping("start")
    public Result<Object> start() {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().singleResult();
        //runtimeService.startProcessInstanceById()
        return new Result<>(null);
    }
}
