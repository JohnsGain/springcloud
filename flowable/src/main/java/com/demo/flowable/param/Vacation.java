package com.demo.flowable.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * author: tangj <br>
 * date: 2019-04-04 15:20 <br>
 * description: 用于流程的Serializable实体最好都定义一个固定的serialVersionUID，因为后期业务变动可能出现
 * 新增字段的场景，如果serialVersionUID固定那么反序列化是ok的，否则的报错。
 * 另外修改字段名或字段类型以及减少字段都会导致序列号失败，导致历史数据不能查询
 */
@Data
@ApiModel("假期相关参数实体")
public class Vacation implements Serializable {

    private static final long serialVersionUID = -2149936066815696579L;

    @ApiModelProperty(value = "请假起始时间", example = "2019-03-12")
    private LocalDate start;

    @ApiModelProperty(value = "请假结束时间", example = "2019-03-15")
    private LocalDate end;

}
