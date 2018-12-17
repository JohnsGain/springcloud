package com.shulian.zuul.config;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhangjuwa
 * @Description:
 * @date 2018/6/20
 * @since jdk1.8
 */
@Component
public class PostZuulFilter extends ZuulFilter {

    private final Logger logger = LoggerFactory.getLogger(PreZuulFilter.class);
    @Override
    public String filterType() {
        return ZuulFilterTypeEnum.post.name();
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String requestURI = request.getRequestURI();
        logger.info(requestURI + " 返回响应, 耗时 {}", (System.currentTimeMillis() - PreZuulFilter.delayTime.get()));
        return null;
    }
}
