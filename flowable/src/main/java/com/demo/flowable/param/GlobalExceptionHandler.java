package com.demo.flowable.param;

import com.alibaba.fastjson.JSONObject;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * @author ddb
 * @update zhangjuwa 2018-11-23
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public JSONObject methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        JSONObject resp = new JSONObject();
        resp.put("status", HttpStatus.BAD_REQUEST.value());
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message;
        if (fieldError != null) {
            message = fieldError.getDefaultMessage();
        } else {
            message = e.getMessage();
        }
        resp.put("message", message);
        return resp;
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public JSONObject bindExceptionExceptionHandler(BindException e) {
        JSONObject resp = new JSONObject();
        resp.put("status", HttpStatus.BAD_REQUEST.value());
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message;
        if (fieldError != null) {
            message = fieldError.getDefaultMessage();
        } else {
            message = e.getMessage();
        }
        resp.put("message", message);
        logger.error(message, e);
        return resp;
    }

    /**
     * @param e
     * @return
     * @update zhangjuwa 2018-12-06 用于处理{@link org.hibernate.validator.constraints.NotBlank}注解验证失败抛出的异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public JSONObject constraintViolationExceptionHandler(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        StringBuilder error = new StringBuilder();
        JSONObject resp = new JSONObject();
        resp.put("status", HttpStatus.BAD_REQUEST.value());
        for (ConstraintViolation<?> violation : constraintViolations) {
            ConstraintViolationImpl constraintViolation = (ConstraintViolationImpl) violation;
            if (error.length() > 0) {
                error.append(",");
            }
            error.append(constraintViolation.getMessageTemplate());
        }
        resp.put("message", error.toString());
        logger.error(error.toString(), e);
        return resp;
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    @ResponseBody
    public JSONObject accessMissingServletRequestPartExceptionHandler(MissingServletRequestPartException e) {
        JSONObject resp = new JSONObject();
        resp.put("status", HttpStatus.BAD_REQUEST.value());
        resp.put("message", "网络异常，请重试");
        logger.info(e.getMessage(), e);
        return resp;
    }

//    @ExceptionHandler(AccessDeniedException.class)
//    @ResponseBody
//    public JSONObject accessDeniedExceptionHandler(AccessDeniedException e) {
//        JSONObject resp = new JSONObject();
//        resp.put("status", HttpStatus.FORBIDDEN.value());
//        resp.put("message", "权限不足");
//        logger.error("权限不足", e);
//        return resp;
//    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public JSONObject httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e) {
        JSONObject resp = new JSONObject();
        resp.put("status", HttpStatus.BAD_REQUEST.value());
        resp.put("message", e.getMessage());
        logger.error(e.getMessage(), e);
        return resp;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public JSONObject methodArgumentTypeMismatchExceptionHandler(HttpRequestMethodNotSupportedException e
    ) {
        JSONObject resp = new JSONObject();
        resp.put("status", HttpStatus.BAD_REQUEST.value());
        resp.put("message", e.getMessage());
        logger.error(e.getMessage(), e);
        return resp;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public JSONObject methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException e) {
        JSONObject resp = new JSONObject();
        resp.put("status", HttpStatus.BAD_REQUEST.value());
        resp.put("message", "参数类型不对");
        logger.error(e.getMessage(), e);
        return resp;
    }

    @ExceptionHandler(ServletException.class)
    @ResponseBody
    public JSONObject ServletExceptionHandler(ServletException e, HttpServletRequest request) {
        JSONObject resp = new JSONObject();
        resp.put("status", HttpStatus.BAD_REQUEST.value());
        resp.put("message", "参数类型不对");
        logger.error("url:" + request.getRequestURL() + " message:" + e.getMessage());
        logger.error(e.getMessage(), e);
        return resp;
    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public JSONObject missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e,
                                                                     HttpServletRequest request) {
        JSONObject resp = new JSONObject();
        resp.put("status", HttpStatus.BAD_REQUEST.value());
        resp.put("message", e.getMessage());
        logger.error("url:" + request.getRequestURL() + " message:" + e.getMessage());
        logger.error(e.getMessage(), e);
        return resp;
    }


}
