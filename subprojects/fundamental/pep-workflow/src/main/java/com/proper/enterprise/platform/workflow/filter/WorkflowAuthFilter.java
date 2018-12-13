package com.proper.enterprise.platform.workflow.filter;

import org.flowable.common.engine.impl.identity.Authentication;

import javax.servlet.*;
import java.io.IOException;

/**
 * 流程登录人与系统登录人做一个同步
 * 拦截器后于AccessTokenFilter
 * 发现请求后将我们的当前登录人同步至flowable
 */
public class WorkflowAuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Authentication.setAuthenticatedUserId(com.proper.enterprise.platform.core.security.Authentication.getCurrentUserId());
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
