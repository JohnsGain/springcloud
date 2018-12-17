package com.shulian.zuul.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhangjuwa
 * @Description:
 * @date 2018/6/13
 * @since jdk1.8
 */
@Component
public class MyInterceptor implements HandlerInterceptor {

    private static ThreadLocal<Long> bonus = new ThreadLocal<Long>(){
        @Override
        protected Long initialValue() {
            return 0L;
        }
    };
    private final Logger logger = LoggerFactory.getLogger(MyInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        long prehandle = System.currentTimeMillis();
        bonus.set(prehandle);
        String requestURI = httpServletRequest.getRequestURI();
        logger.info(requestURI + "进入服务器:" + prehandle);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        String requestURI = httpServletRequest.getRequestURI();
        long postHandle = System.currentTimeMillis();
        logger.info(requestURI + "控制器返回响应:" + postHandle + ",共花费" + (postHandle - bonus.get()));
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        String requestURI = httpServletRequest.getRequestURI();
        long completion = System.currentTimeMillis();
        logger.info(requestURI + "页面渲染完成:" + completion + ",共花费" + (completion - bonus.get()));
    }
}
