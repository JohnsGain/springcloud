package com.shulian.neo4j.controller;

import com.shulian.neo4j.dto.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Profile({"dev", "test"})
@RestControllerAdvice
public class DevExceptionHandler {
    private static Logger logger = LoggerFactory.getLogger(DevExceptionHandler.class);

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        logger.error(e.getMessage(), e);
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message;
        if (fieldError != null) message = fieldError.getDefaultMessage();
        else message = e.getMessage();
        return Result.<String>build()
                .withCode(HttpStatus.BAD_REQUEST.value())
                .withMessage(message);
    }

    /**
        * @Description:接口单个参数的校验捕捉处理
        * @author biaoyang
        * @date 2018/8/15 0015 14:32
        */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<String> handleValidationException(ConstraintViolationException e) {
        for (ConstraintViolation<?> s : e.getConstraintViolations()) {
            return Result.<String>build().withCode(HttpStatus.BAD_REQUEST.value())
                    .withMessage(s.getMessage());
        }
        return null;
    }

    /**
     * 趋势研究-获取参数验证返回的错误信息
     * @auther zhangjuwa
     * @date 2018-09-05
     * @param e
     * @return 返回参数验证产生的错误信息
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BindException.class})
    public Result<String> bindExceptionHandler(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        StringBuilder errors = new StringBuilder();
        for (FieldError fieldError : fieldErrors) {
            logger.error(fieldError.getObjectName() + " : " + fieldError.getDefaultMessage());
            if (errors.length() > 0) {
                errors.append(",");
            }
            errors.append(fieldError.getDefaultMessage());
        }
        return Result.<String>build()
                .withCode(HttpStatus.BAD_REQUEST.value())
                .withMessage(errors.toString());
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<String> methodArgumentTypeMismatchExceptionHandler(HttpRequestMethodNotSupportedException e) {
        logger.error(e.getMessage(), e);
        return Result.<String>build()
                .withCode(HttpStatus.BAD_REQUEST.value())
                .withMessage(e.getMessage());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public Result<String> methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException e) {
        logger.error(e.getMessage(), e);
        return Result.<String>build()
                .withCode(HttpStatus.BAD_REQUEST.value())
                .withMessage(e.getMessage());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<String> missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e) {
        logger.error(e.getMessage(), e);
        return Result.<String>build()
                .withCode(HttpStatus.BAD_REQUEST.value())
                .withMessage(e.getMessage());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(RuntimeException.class)
    public Result<String> exceptionHandler(RuntimeException e) {
        logger.error(e.getMessage(), e);
        return Result.<String>build()
                .withCode(HttpStatus.BAD_REQUEST.value())
                .withMessage(e.getMessage());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    public Result<String> exceptionHandler(Exception e) {
        logger.error(e.getMessage(), e);
        return Result.<String>build()
                .withCode(HttpStatus.BAD_REQUEST.value())
                .withMessage(e.getMessage());
    }

}
