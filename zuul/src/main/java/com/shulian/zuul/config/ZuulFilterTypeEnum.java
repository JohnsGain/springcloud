package com.shulian.zuul.config;

/**
 * @author ""
 * @since jdk1.8
 */
public enum ZuulFilterTypeEnum {
    /**
     * 可以在请求被路由之前调用
     */
    pre,
    /**
     * 请求处理完成后执行的filter
     */
    post,
    /**
     * 处理请求，进行路由
     */
    route,
    /**
     * 出现错误时执行的filter
     */
    error;
}
