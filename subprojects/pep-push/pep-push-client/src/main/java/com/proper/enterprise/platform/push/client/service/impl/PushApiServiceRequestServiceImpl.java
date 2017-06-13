package com.proper.enterprise.platform.push.client.service.impl;

import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.core.utils.http.HttpClient;
import com.proper.enterprise.platform.push.client.service.IPushApiServiceRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Map;

/**
 * 默认的http请求方法
 *
 * @author shen
 *
 */
public class PushApiServiceRequestServiceImpl implements IPushApiServiceRequest {

    @Override
    public synchronized String requestServiceServer(final String baseUrl, final String methodName,
            Map<String, Object> params, int timeout) throws Exception {
        String url = baseUrl + "/appserver/request/" + methodName;
        String data = HttpClient.getFormUrlEncodedData(params);
        ResponseEntity<byte[]> response = HttpClient.post(url, MediaType.APPLICATION_FORM_URLENCODED, data);
        return StringUtil.toEncodedString(response.getBody());
    }



}
