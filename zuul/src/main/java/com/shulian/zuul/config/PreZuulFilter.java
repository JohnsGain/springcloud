package com.shulian.zuul.config;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.function.Supplier;

/**
 * @author ""
 * @description 测试Zuul服务安全过滤
 * @date 2018/4/20
 * @since jdk1.8
 */
@Component
public class PreZuulFilter extends ZuulFilter {

    public static ThreadLocal<Long> delayTime = ThreadLocal.withInitial(() -> 0L);
    private final Logger logger = LoggerFactory.getLogger(PreZuulFilter.class);

    /**
     * 服务过滤类型共四种:pre,post,route,error
     * @return
     */
    @Override
    public String filterType() {
        return ZuulFilterTypeEnum.pre.name();
    }

    /**
     * 过滤顺序
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 可以写逻辑
     * 判断是否应该过滤
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 执行的过滤逻辑，可以进行权限认证，参数验证
     * @return
     */
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String requestURI = request.getRequestURI();
        long prehandle = System.currentTimeMillis();
        delayTime.set(prehandle);
        logger.info(requestURI + "进入服务器:");
        logger.info(String.format("%s >>> %s ", request.getMethod(), request.getRequestURL().toString()));
//        String token = request.getParameter("token");
//        if (token == null) {
//            logger.warn("token is null！");
//            ctx.setResponseStatusCode(401);
//            ctx.setSendZuulResponse(false);
//
//            try {
//                ctx.getResponse().getWriter().write("token is emtpy!");
//            } catch (IOException e) {
//                return null;
//            }
//        } else {
//            logger.info("ok, can access");
//        }

        return null;
    }
}
