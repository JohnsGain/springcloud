package com.jpa.demo.param;

import com.fasterxml.jackson.annotation.JsonRawValue;

/**
 * @author Lee
 * @date 2018年10月10日 上午10:54:06
 */
public class ResponseVO {

    // 状态码
    private Integer code;
    // 是否成功
    private Boolean success;
    // 返回信息
    private String message;
    // 返回实体结果
    private Object data;

    protected static final String successMessage = "请求成功";

    public static ResponseVO success() {
        return new ResponseVO(0, true, successMessage, null);
    }

    public static ResponseVO success(Object data) {
        return new ResponseVO(0, true, successMessage, data);
    }

    /**
     * @param data   数据
     * @param isJson 传进来的data是否为一个json字符串
     * @return
     */
    public static ResponseVO success(String data, Boolean isJson) {
        if (isJson != null && isJson)
            return new JsonResponseVO(0, true, successMessage, data);
        return new ResponseVO(0, true, successMessage, data);
    }

    public static ResponseVO success(StringData data) {
        return success(data.getData(), data.getIsJson());
    }

    public static ResponseVO failure(Integer code) {
        return new ResponseVO(code, false, "处理失败", null);
    }

    public static ResponseVO failure(Integer code, String message) {
        return new ResponseVO(code, false, message, null);
    }

    protected ResponseVO(Integer code, Boolean success, String message, Object data) {
        super();
        this.code = code;
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    private static class JsonResponseVO extends ResponseVO {

        // 已经被json序列化的字符串
        @JsonRawValue
        private String data;

        private JsonResponseVO(Integer code, Boolean success, String message, String jsonData) {
            super(code, success, message, null);
            this.data = jsonData;
        }

        @Override
        public String getData() {
            return data;
        }

    }

}
