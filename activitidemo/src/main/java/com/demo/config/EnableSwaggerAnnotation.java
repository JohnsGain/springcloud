package com.demo.config;

import java.lang.annotation.*;

/**
 * @author zhangjuwa
 * @date 2019/5/17
 * @since jdk1.8
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
public @interface EnableSwaggerAnnotation {

    String[] basePackage();

    String name() default "swagger";
}
