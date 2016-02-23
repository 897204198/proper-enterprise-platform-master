package com.proper.enterprise.platform.api.auth;

import com.proper.enterprise.platform.core.api.IBase;
import com.proper.enterprise.platform.api.auth.enums.ResourceType;
import org.springframework.web.bind.annotation.RequestMethod;

public interface Resource extends IBase {

    String getCode();

    void setCode(String code);

    String getUrl();

    void setUrl(String url);

    RequestMethod getMethod();

    void setMethod(RequestMethod method);

    String getName();

    void setName(String name);

    Resource getParent();

    void setParent(Resource parent);

    ResourceType getResourceType();

    void setResourceType(ResourceType resourceType);

    int getSequenceNumber();

    void setSequenceNumber(int sequenceNumber);

}
