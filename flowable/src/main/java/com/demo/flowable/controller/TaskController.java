package com.demo.flowable.controller;

import com.alibaba.fastjson.JSON;
import com.demo.flowable.param.CommentVO;
import com.demo.flowable.param.ProcessStatus;
import com.demo.flowable.param.Result;
import com.demo.flowable.param.TaskVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.flowable.engine.FormService;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.form.FormProperty;
import org.flowable.engine.form.StartFormData;
import org.flowable.engine.form.TaskFormData;
import org.flowable.engine.impl.form.EnumFormType;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.variable.api.persistence.entity.VariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.demo.flowable.controller.ServiceConst.APPROVED_VARIABLE_NAME;

/**
 * @author zhangjuwa
 * @date 2019/6/2
 * @since jdk1.8
 **/
@RestController
@RequestMapping("task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private FormService formService;

    @Autowired
    private IdentityService identityService;

    @ApiOperation("当前待完成的任务")
    @GetMapping("page")
    public Result<PageInfo<TaskVO>> page(@ApiIgnore @PageableDefault Pageable pageable) {

        List<Task> tasks = taskService.createTaskQuery()
                .active()
                .listPage(pageable.getPageNumber(), pageable.getPageSize());
        List<TaskVO> taskVOS = new LinkedList<>();
        for (Task task : tasks) {
            TaskVO taskVO = new TaskVO();
            taskVO.setCreateTime(task.getCreateTime());
            taskVO.setVariables(task.getProcessVariables());
            taskVO.setId(task.getId());
            taskVO.setProcessInstanceId(task.getProcessInstanceId());
//            taskVO.setProcessDefinitionKey(task.get());
            taskVO.setAssign(task.getAssignee());
            String owner = task.getOwner();
            taskVO.setStarter(owner);
            taskVOS.add(taskVO);

        }
        PageInfo<TaskVO> of = PageInfo.of(taskVOS);
        of.setPageNum(pageable.getPageNumber());
        of.setPageSize(pageable.getPageSize());
        return new Result<>(of);
    }

    @ApiOperation("当前待分配的任务")
    @GetMapping("unassign")
    public Result<PageInfo<TaskVO>> unassign(@ApiIgnore @PageableDefault Pageable pageable) {
        Page<TaskVO> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        page.setReasonable(true);
        long count = taskService.createTaskQuery()
                .taskUnassigned()
                .active()
                .count();
        page.setTotal(count);
        List<Task> tasks = taskService.createTaskQuery()
                .taskUnassigned()
                .active()
                .listPage(page.getStartRow(), pageable.getPageSize());

        for (Task task : tasks) {
            TaskVO taskVO = new TaskVO();
            taskVO.setCreateTime(task.getCreateTime());
            taskVO.setVariables(task.getProcessVariables());
            taskVO.setId(task.getId());
            taskVO.setProcessInstanceId(task.getProcessInstanceId());
//            taskVO.setProcessDefinitionKey(task.get());
            taskVO.setAssign(task.getAssignee());
            String owner = task.getOwner();
            taskVO.setStarter(owner);
            page.add(taskVO);
        }
        PageInfo<TaskVO> taskVOPageInfo = new PageInfo<>(page);
        return new Result<>(taskVOPageInfo);
    }

    @ApiOperation("任务分配")
    @PutMapping("assign")
    public Result<String> assign(String taskId, String assignerId) {
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();
        if (task == null || task.getAssignee() != null) {
            throw new IllegalStateException("当前任务不存在或已被分配");
        }
        taskService.claim(taskId, assignerId);
        return new Result<>("success");
    }

    @ApiOperation("查看我的任务")
    @GetMapping
    public Result<List<TaskVO>> myTask(@RequestParam String userId) {
        List<Task> list = taskService.createTaskQuery()
                .active()
                .taskAssignee(userId)
                .list();
        List<TaskVO> collect = list.stream()
                .map(task -> {
                    TaskVO taskVO = new TaskVO();
                    taskVO.setCreateTime(task.getCreateTime());
                    taskVO.setVariables(task.getProcessVariables());
                    taskVO.setId(task.getId());
                    taskVO.setProcessInstanceId(task.getProcessInstanceId());
                    taskVO.setAssign(task.getAssignee());
                    String owner = task.getOwner();
                    taskVO.setStarter(owner);
                    return taskVO;
                })
                .collect(Collectors.toList());
        return new Result<>(collect);
    }

    @ApiOperation("完成任务")
    @PutMapping
    public Result<String> complete(String taskId, CommentVO comment, String userId) {
        TaskQuery taskQuery = taskService.createTaskQuery();
        Task task = taskQuery.taskAssignee(userId).taskId(taskId).singleResult();
        if (task == null) {
            throw new RuntimeException("任务不存在或者你没有执行该任务的权限！");
        }

        //获取当前任务拥有的变量参数
        Map<String, VariableInstance> variableInstxances = taskService.getVariableInstances(taskId);

        //获取某个流程定义启动的时候需要填写的表单属性，就是启动的时候要传递的参数
        StartFormData startFormData = formService.getStartFormData(task.getProcessDefinitionId());
        List<FormProperty> formProperties = startFormData.getFormProperties();


        boolean approve = comment.isApprove();
        Map<String, Object> variables = new HashMap<>();

        //获取完成某个任务需要涉及到的表单属性，就是要传递的参数
        TaskFormData taskFormData = formService.getTaskFormData(taskId);
        List<FormProperty> formProperties1 = taskFormData.getFormProperties();
        formProperties1.forEach(item -> {

            if ("enum".equalsIgnoreCase(item.getType().getName())){
                variables.put(item.getId(), comment.isApprove());
            } else {
                variables.put(item.getId(), comment.getContent());
            }

        });
        variables.put(APPROVED_VARIABLE_NAME, approve);
        if (!approve) {
            variables.put(ServiceConst.STATUS_VARIABLE_NAME, ProcessStatus.NOT_PASSED);
        }

        try {
            identityService.setAuthenticatedUserId(userId);
            taskService.addComment(taskId, task.getProcessInstanceId(), JSON.toJSONString(comment));
            taskService.complete(taskId, variables);
        } finally {
            identityService.setAuthenticatedUserId(null);
        }
        return new Result<>("success");
    }

    @ApiOperation("获取任务审批意见")
    @GetMapping("comments/{taskId}")
    public Result<List<CommentVO>> getTaskComments(@PathVariable String taskId) {
        List<CommentVO> collect = taskService.getTaskComments(taskId).stream()
                .map(a -> JSON.parseObject(a.getFullMessage(), CommentVO.class))
                .collect(Collectors.toList());

        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .taskUnassigned()
                .singleResult();
//        DelegationState delegationState = task.getDelegationState();
//        delegationState.
        return new Result<>(collect);
    }
}
