package com.shulian.neo4j.dto;

import org.springframework.data.neo4j.mapping.Neo4jPersistentProperty;
import org.springframework.http.HttpStatus;

/**
 * @param <T>
 */
public class Result<T> {

    private int code = 200;

    private String message = "OK";

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
        //Neo4jPersistentProperty
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
        this.message = HttpStatus.OK.getReasonPhrase();
        return this;
    }

    /**
     * 系统内部错误
     *
     * @return this
     */
    public Result<T> error() {
        this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.message = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
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
