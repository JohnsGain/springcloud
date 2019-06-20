package com.demo.flowable.param;

/**
 * author: tangj <br>
 * date: 2019-04-15 14:28 <br>
 * description: 流程状态
 */
public interface ProcessStatus {

    /**
     * 审核通过
     */
    Integer PASSED = 1;

    /**
     * 流程结束--未通过
     */
    Integer NOT_PASSED = -1;

    /**
     * 审批中
     */
    Integer IN_PROCESS = 0;
}
