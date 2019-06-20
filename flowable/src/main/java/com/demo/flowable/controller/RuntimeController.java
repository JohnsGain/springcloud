package com.demo.flowable.controller;

import com.demo.flowable.param.Result;
import com.demo.flowable.param.Vacation;
import io.swagger.annotations.ApiOperation;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangjuwa
 * @date 2019/6/2
 * @since jdk1.8
 **/
@RestController
@RequestMapping("runtime")
public class RuntimeController {

    private final RuntimeService runtimeService;

    /**
     * 它用于管理（创建，更新，删除，查询……）组与用户
     */
    private final IdentityService identityService;

    @Autowired
    public RuntimeController(RuntimeService runtimeService, IdentityService identityService) {
        this.runtimeService = runtimeService;
        this.identityService = identityService;
    }

    @ApiOperation("启动流程")
    @PostMapping
    public Result<String> start(@RequestParam String key) {
        String currentUserid = "1";
        identityService.setAuthenticatedUserId(currentUserid);
        Map<String, Object> map = new HashMap<>();
        Vacation vacation = new Vacation();
        vacation.setEnd(LocalDate.of(2019, 7, 1));
        vacation.setStart(LocalDate.now());
        map.put("params", vacation);
        map.put("startDate", LocalDateTime.now());
        map.put("employeeName", "john");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(key, map);
        if (processInstance != null) {
            return new Result(processInstance.getId());
        }
        return new Result("failure");
    }
}
