package com.demo.flowable.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@ApiModel("流程实例实体")
@Data
public class ProcessVo {

    @ApiModelProperty("流程实例id")
    private String id;

    @ApiModelProperty("流程发起时间")
    private Date start;

    @ApiModelProperty("流程参数")
    private Object data;

    private Integer status;

    Map<String, Object> variables;
}
