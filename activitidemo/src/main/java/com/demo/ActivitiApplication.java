package com.demo;

import com.demo.config.EnableSwaggerAnnotation;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.rest.common.api.DataResponse;
import org.activiti.rest.service.api.identity.UserCollectionResource;
import org.activiti.rest.service.api.repository.ModelCollectionResource;
import org.activiti.rest.service.api.repository.ProcessDefinitionCollectionResource;
import org.activiti.rest.service.api.repository.ProcessDefinitionResource;
import org.activiti.rest.service.api.runtime.process.ProcessInstanceCollectionResource;
import org.activiti.rest.service.api.runtime.process.ProcessInstanceResource;
import org.activiti.rest.service.api.runtime.task.TaskCollectionResource;
import org.activiti.rest.service.api.runtime.task.TaskCommentCollectionResource;
import org.activiti.rest.service.api.runtime.task.TaskIdentityLinkCollectionResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author ""
 * @date 2019/3/15
 * @since jdk1.8
 **/
@SpringBootApplication
@ComponentScan(basePackages = {"org.activiti.spring", "com.demo"})
@EnableSwaggerAnnotation(basePackage = "com.demo")
public class ActivitiApplication implements ApplicationListener<ApplicationReadyEvent> {

    public static void main(String[] args) {

        SpringApplication.run(ActivitiApplication.class, args);
    }

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
//        DataResponse
//        ModelCollectionResource
//        UserCollectionResource
//        ProcessDefinitionCollectionResource
//        ProcessDefinitionResource
//        TaskCollectionResource
//        ProcessInstanceCollectionResource
//        ProcessInstanceResource
//        TaskIdentityLinkCollectionResource
//        TaskCommentCollectionResource
//        taskService.add
        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
        ProcessInstance processInstance = processInstanceQuery.singleResult();
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("processes/test.task_bpmn20.xml")
                .deploy();
    }
}
