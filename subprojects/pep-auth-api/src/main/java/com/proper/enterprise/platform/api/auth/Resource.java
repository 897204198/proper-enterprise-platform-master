package com.proper.enterprise.platform.api.auth;

import org.springframework.web.bind.annotation.RequestMethod;

public interface Resource {

    String getId();

    void setId(String id);

    String getUrl();

    void setUrl(String url);

    RequestMethod getMethod();

    void setMethod(RequestMethod method);
    
}
