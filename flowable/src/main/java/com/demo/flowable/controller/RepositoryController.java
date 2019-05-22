package com.demo.flowable.controller;

import com.demo.flowable.dto.ProcessDefinitionOutput;
import com.demo.flowable.param.Result;
import io.swagger.annotations.Api;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

/**
 * @author zhangjuwa
 * @date 2019/5/22
 * @since jdk1.8
 **/
@RestController
@RequestMapping("repository")
@Api(tags = "RepositoryService使用")
public class RepositoryController {

    private final RepositoryService repositoryService;

    @Autowired
    public RepositoryController(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @GetMapping("processDefinitions")
    public Result<Page<ProcessDefinitionOutput>> page(@PageableDefault Pageable pageable) {
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        long count = processDefinitionQuery.active().count();
        List<ProcessDefinition> processDefinitions = processDefinitionQuery.active()
                .orderByDeploymentId().desc()
                .listPage((int) pageable.getOffset(), pageable.getPageSize());
        List<ProcessDefinitionOutput> list = new LinkedList<>();
        for (ProcessDefinition item : processDefinitions) {
            ProcessDefinitionOutput output = new ProcessDefinitionOutput();
            output.setCategory(item.getCategory());
            output.setId(item.getId());
            output.setName(item.getName());
            output.setDeploymentId(item.getDeploymentId());
            output.setKey(item.getKey());
            output.setDescription(item.getDescription());
            output.setStartFormKey(item.hasStartFormKey());
            output.setSuspended(item.isSuspended());
            output.setDiagramResourceName(item.getDiagramResourceName());
            list.add(output);
        }
        Page<ProcessDefinitionOutput> out = new PageImpl<>(list, pageable, count);
//        out.
        return new Result<>(out);
    }
}
