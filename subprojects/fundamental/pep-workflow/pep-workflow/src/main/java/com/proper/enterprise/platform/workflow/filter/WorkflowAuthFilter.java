package com.proper.enterprise.platform.workflow.filter;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.core.PEPApplicationContext;
import com.proper.enterprise.platform.workflow.convert.UserConvert;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.ui.common.security.SecurityUtils;

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
        //流程权限
        Authentication.setAuthenticatedUserId(com.proper.enterprise.platform.core.security.Authentication.getCurrentUserId());
        //流程表单设计器权限
        User pepUser = PEPApplicationContext.getBean(UserService.class)
            .get(com.proper.enterprise.platform.core.security.Authentication.getCurrentUserId());
        if (null != pepUser) {
            SecurityUtils.assumeUser(UserConvert.convert(pepUser));
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
