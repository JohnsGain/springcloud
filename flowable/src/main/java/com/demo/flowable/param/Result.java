package com.demo.flowable.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author Hero
 * @date 7/12/2017
 */
@ApiModel("返回结果集")
public class Result<T> {

    @ApiModelProperty("返回码")
    private int code = 200;
    @ApiModelProperty("返回消息")
    private String message = "OK";
    @ApiModelProperty("数据")
    private T data;

    public static <T> Result<T> build() {
        return new Result<T>();
    }

    private Result() {
    }

    public Result(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Result<T> withCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Result<T> withMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Result<T> withData(T data) {
        this.data = data;
        return this;
    }

    /**
     * 操作成功
     *
     * @return this
     */
    public Result<T> ok() {
        this.code = ResultStatusEnum.OK.getCode();
        this.message = ResultStatusEnum.OK.getMessage();
        return this;
    }

    /**
     * 系统内部错误
     *
     * @return this
     */
    public Result<T> error() {
        this.code = ResultStatusEnum.INTER_ERROR.getCode();
        this.message = ResultStatusEnum.INTER_ERROR.getMessage();
        return this;
    }

    /**
     * 操作错误
     *
     * @param statusEnum 状态信息
     * @return this
     */
    public Result<T> error(ResultStatusEnum statusEnum) {
        this.code = statusEnum.getCode();
        this.message = statusEnum.getMessage();
        return this;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
