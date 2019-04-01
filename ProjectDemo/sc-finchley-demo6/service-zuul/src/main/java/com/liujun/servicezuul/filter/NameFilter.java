package com.liujun.servicezuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class NameFilter extends ZuulFilter {
    private static Logger log = LoggerFactory.getLogger(NameFilter.class);
    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        //从上下文获取filter-is-success值，用于判断此Filter是否执行
        return (boolean)ctx.get("filter-is-success");
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        log.info(String.format("%s -----------> %s", request.getMethod(), request.getRequestURL().toString()));

        String name = request.getParameter("name");// 获取请求的参数
        if(null != name && name.equals("liujun")) {
            ctx.setSendZuulResponse(true);// 是否允许该请求对下游进行路由
            ctx.setResponseStatusCode(200);

            return null;
        }else {
            ctx.setSendZuulResponse(false);// 是否允许该请求对下游进行路由
            ctx.setResponseStatusCode(401);// 返回错误码
            ctx.setResponseBody("{\"status\":401,\"message\":\"非管理员用户！\"}");//返回错误内容

            return null;
        }
    }
}
