package com.proper.enterprise.platform.push.client

import com.proper.enterprise.platform.push.client.service.IPushApiServiceRequest

class PushApiServiceRequestMockService implements IPushApiServiceRequest {
    @Override
    public String requestServiceServer(String baseUrl, String methodName, Map<String, Object> params, int timeout)
    throws Exception {
        return "";
    }
}
