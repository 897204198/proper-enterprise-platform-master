package com.proper.enterprise.platform.workflow.filter;

import com.proper.enterprise.platform.core.security.Authentication;
import org.springframework.stereotype.Service;
import javax.servlet.*;
import java.io.IOException;

@Service("wfAuthFilter")
public class WorkflowAuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        org.flowable.engine.common.impl.identity.Authentication.setAuthenticatedUserId(Authentication.getCurrentUserId());
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
