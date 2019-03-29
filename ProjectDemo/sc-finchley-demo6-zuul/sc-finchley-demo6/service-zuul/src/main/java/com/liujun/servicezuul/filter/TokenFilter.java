package com.liujun.servicezuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class TokenFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(TokenFilter.class);
    @Override
    public boolean shouldFilter() {
        return true;// 是否执行该过滤器，此处为true，说明需要过滤
    }

    @Override
    public int filterOrder() {
        return 0;// 优先级0，数字越大，优先级越低
    }

    @Override
    public String filterType() {
        return "pre";// 前置过滤器
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        log.info(String.format("%s -----------> %s", request.getMethod(), request.getRequestURL().toString()));

        Object accessToken = request.getParameter("token");// 获取请求的参数
        if(accessToken == null) {
            log.warn("token is empty");

            ctx.setSendZuulResponse(false);// 对该请求进行路由
            ctx.setResponseStatusCode(401);// 返回错误码

            try {
                ctx.getResponse().getWriter().write("token is empty");
            }catch (Exception e){}

            return null;
        }
        log.info("token   ok");
        return null;
    }
}