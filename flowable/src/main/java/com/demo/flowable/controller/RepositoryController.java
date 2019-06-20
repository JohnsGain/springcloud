package com.demo.flowable.controller;

import com.demo.flowable.dto.ProcessDefinitionOutput;
import com.demo.flowable.param.Result;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @ApiOperation("获取流程定义实例")
    @GetMapping("processDefinitions")
    public Result<PageInfo<ProcessDefinitionOutput>> page(@PageableDefault Pageable pageable) {
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
        PageInfo<ProcessDefinitionOutput> of = PageInfo.of(list);
        of.setPageNum(pageable.getPageNumber());
        of.setPageSize(pageable.getPageSize());
        return new Result<>(of);
    }


}
