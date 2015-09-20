package com.proper.enterprise.platform.api.auth;

import com.proper.enterprise.platform.core.api.IBase;
import org.springframework.web.bind.annotation.RequestMethod;

public interface Resource extends IBase {

    String getUrl();

    void setUrl(String url);

    RequestMethod getMethod();

    void setMethod(RequestMethod method);

    String getName();

    void setName(String name);
    
}
