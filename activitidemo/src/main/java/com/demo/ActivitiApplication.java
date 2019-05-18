package com.demo;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
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
public class ActivitiApplication implements ApplicationListener<ApplicationReadyEvent> {

    public static void main(String[] args) {

        SpringApplication.run(ActivitiApplication.class, args);
    }

    @Autowired
    private RepositoryService repositoryService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("processes/test.task_bpmn20.xml")
                .deploy();
    }
}
