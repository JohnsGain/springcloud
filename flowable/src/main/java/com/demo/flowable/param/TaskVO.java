package com.demo.flowable.param;

import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * author: tangj <br>
 * date: 2019-04-09 11:21 <br>
 * description:
 */
@Data
public class TaskVO {

    private Date createTime;

    private Date endTime;

    private String id;

    private String starter;

    private Vacation vacation;

    Map<String, Object> variables;

    private String processDefinitionKey;

    private String processInstanceId;

    private String assign;

}
