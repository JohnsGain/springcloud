package eureka.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.http.HttpStatus;

/**
 *
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
    public static <T> Result<T> build(T t) {
    	return new Result<T>(t);
    }

    private Result() {
    }

    public Result(T t) {
    	this.data = t;
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
        this.code = HttpStatus.OK.value();
        this.message = "success";
        return this;
    }

    /**
     * 系统内部错误
     *
     * @return this
     */
    public Result<T> error() {
        this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.message = "服务内部错误";
        return this;
    }

    /**
     * 操作错误
     *
     * @param status 状态信息
     * @return this
     */
    public Result<T> error(HttpStatus status) {
        this.code = status.value();
        this.message = status.getReasonPhrase();
        return this;
    }
}
