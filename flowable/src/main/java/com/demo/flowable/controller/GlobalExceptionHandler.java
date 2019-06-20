package com.demo.flowable.controller;

import com.demo.flowable.param.Result;
import org.flowable.common.engine.api.FlowableObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
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
import java.nio.file.AccessDeniedException;

/**
 * @author Duhuafei
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public Result accessDeniedExceptionHandler(AccessDeniedException e) {
        log.error(e.getMessage(), e);
        return Result.build()
                .error()
                .withCode(HttpStatus.FORBIDDEN.value())
                .withMessage("权限不足");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Result constraintViolationExceptionHandler(ConstraintViolationException e) {
        log.error(e.getMessage(), e);
        final String message = e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .reduce((s, s2) -> s + ", " + s2)
                .orElse("");
        return Result.build()
                .error()
                .withCode(HttpStatus.BAD_REQUEST.value())
                .withMessage(message);
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message;
        if (fieldError != null) {
            message = fieldError.getDefaultMessage();
        } else {
            message = e.getMessage();
        }
        return Result.build()
                .withCode(HttpStatus.BAD_REQUEST.value())
                .withMessage(message);
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result methodArgumentTypeMismatchExceptionHandler(HttpRequestMethodNotSupportedException e) {
        log.error(e.getMessage(), e);
        return Result.build()
                .withCode(HttpStatus.BAD_REQUEST.value())
                .withMessage(e.getMessage());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public Result methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException e) {
        log.error(e.getMessage(), e);
        return Result.build()
                .withCode(HttpStatus.BAD_REQUEST.value())
                .withMessage("参数类型不匹配");
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public Result<String> httpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error(e.getMessage(), e);
        return Result.<String>build()
                .withCode(HttpStatus.BAD_REQUEST.value())
                .withMessage(e.getMessage());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e) {
        log.error(e.getMessage(), e);
        return Result.build()
                .withCode(HttpStatus.BAD_REQUEST.value())
                .withMessage(e.getMessage());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(HttpMessageNotWritableException.class)
    public Result httpMessageNotWritableExceptionHandler(HttpMessageNotWritableException e) {
        log.error(e.getMessage(), e);
        return Result.build()
                .error()
                .withCode(HttpStatus.BAD_REQUEST.value())
                .withMessage(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public Result exceptionHandler(RuntimeException e) {
        log.error(e.getMessage(), e);
        return Result.build()
                .withCode(HttpStatus.BAD_REQUEST.value())
                .withMessage("网络异常");
    }

    @ExceptionHandler(Exception.class)
    public Result exceptionHandler(Exception e) {
        log.error(e.getMessage(), e);
        return Result.build()
                .withCode(HttpStatus.BAD_REQUEST.value())
                .withMessage("网络异常");
    }

    @ExceptionHandler(FlowableObjectNotFoundException.class)
    public Result flowableObjectNotFoundExceptionHandler(FlowableObjectNotFoundException e) {
        log.error(e.getMessage(), e);
        return Result.build()
                .withCode(HttpStatus.BAD_REQUEST.value())
                .withMessage("不存在这个流程");
    }

    @ExceptionHandler(IllegalStateException.class)
    public Result illegalStateExceptionHandler(IllegalStateException e) {
        log.error(e.getMessage(), e);
        return Result.build()
                .withCode(HttpStatus.BAD_REQUEST.value())
                .withMessage(e.getMessage());
    }

}
