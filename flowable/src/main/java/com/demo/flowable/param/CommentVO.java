package com.demo.flowable.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * author: tangj <br>
 * date: 2019-04-04 15:39 <br>
 * description:
 */
@Data
public class CommentVO {

    @ApiModelProperty("审核内容")
    private String content;

    @ApiModelProperty("是否审批通过")
    private boolean approve;
}
