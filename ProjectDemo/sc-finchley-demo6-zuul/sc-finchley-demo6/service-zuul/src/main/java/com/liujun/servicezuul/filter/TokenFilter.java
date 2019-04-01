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
        return true;// 是否执行该过滤器，此处为true为过滤
    }

    @Override
    public int filterOrder() {
        return 0;// 优先级0，数字越大，优先级越低
    }

    @Override
    public String filterType() {
        return "pre";//pre：路由之前;routing：路由之时;post： 路由之后;error：发送错误调用
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.getResponse().setCharacterEncoding("GBK");

        HttpServletRequest request = ctx.getRequest();
        log.info(String.format("%s -----------> %s", request.getMethod(), request.getRequestURL().toString()));

        Object accessToken = request.getParameter("token");// 获取请求的参数
        if(accessToken == null) {
            ctx.setSendZuulResponse(false);// 是否允许该请求对下游进行路由
            ctx.setResponseStatusCode(401);// 返回错误码
            ctx.setResponseBody("{\"status\":401,\"message\":\"token为空！\"}");//返回错误内容

            //或者也可以后端打印下try {ctx.getResponse().getWriter().write("token is null");}catch (Exception e){}

            //filter-is-success保存于上下文，作为同类型下游Filter的执行开关
            ctx.set("filter-is-success", false);
            return null;
        }

        ctx.set("filter-is-success", true);
        return null;
    }
}