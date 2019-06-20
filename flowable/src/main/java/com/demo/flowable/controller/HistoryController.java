package com.demo.flowable.controller;

import com.demo.flowable.param.ProcessVo;
import com.demo.flowable.param.Result;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.flowable.engine.HistoryService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

/**
 * @author zhangjuwa
 * @date 2019/6/2
 * @since jdk1.8
 **/
@RestController
@RequestMapping("history")
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    @GetMapping("myApply")
    @ApiOperation("我发起的审批")
    public Result<PageInfo<ProcessVo>> myApply(String userId, @ApiIgnore @PageableDefault Pageable pageable) {
        HistoricProcessInstanceQuery desc = historyService.createHistoricProcessInstanceQuery()
                .startedBy(userId)
                .includeProcessVariables()
                .orderByProcessInstanceStartTime().desc();
        Page<ProcessVo> page = new Page<>();

        page.setTotal(desc.count());
        page.setReasonable(true);
        List<HistoricProcessInstance> processInstances =
                desc.listPage(page.getStartRow(), pageable.getPageSize());
        processInstances.forEach(instance -> {
            ProcessVo processVo = new ProcessVo();
            processVo.setId(instance.getId());
            processVo.setStart(instance.getStartTime());
            Map<String, Object> variables = instance.getProcessVariables();
//            processVo.setData(variables.get(DATA_VARIABLE_NAME));
//            processVo.setStatus((Integer) variables.get(STATUS_VARIABLE_NAME));
            processVo.setVariables(variables);
            page.add(processVo);
        });
        PageInfo<ProcessVo> pageInfo = new PageInfo<>(page);
        return new Result<>(pageInfo);
    }
}
