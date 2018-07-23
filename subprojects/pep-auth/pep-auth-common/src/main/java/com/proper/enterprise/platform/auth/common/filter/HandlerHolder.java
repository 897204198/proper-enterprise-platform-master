package com.proper.enterprise.platform.auth.common.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;

@Service
public class HandlerHolder {

    @Autowired
    RequestMappingHandlerMapping mapping;

    public HandlerMethod getHandler(HttpServletRequest request) throws Exception {
        return (HandlerMethod) mapping.getHandler(request).getHandler();
    }

}
