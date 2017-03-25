package com.proper.enterprise.platform.push.client.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.utils.http.HttpClient;
import com.proper.enterprise.platform.push.client.service.IPushApiServiceRequest;

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
        final String defaultCharset = PEPConstants.DEFAULT_CHARSET.name();
        String result = "";
        String url = baseUrl + "/appserver/request/" + methodName;
        String data = getRequestData(params, defaultCharset).toString();
        ResponseEntity<byte[]> response = HttpClient.post(url, MediaType.APPLICATION_FORM_URLENCODED, data);
        byte[] rbytes = response.getBody();
        if (rbytes != null) {
            result = new String(rbytes, defaultCharset);
        }
        return result;
    }

    /**
     * 将请求参数map转换成form表单APPLICATION_FORM_URLENCODED字符串
     * 
     * @param params
     *            请求参数map
     * @param encode
     *            字符串编码格式
     * @return 存储APPLICATION_FORM_URLENCODED的字符串
     * @throws UnsupportedEncodingException
     */
    public static String getRequestData(Map<String, Object> params, String encode) throws UnsupportedEncodingException {
        StringBuffer stringBuffer = new StringBuffer(); // 存储封装好的请求体信息
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            stringBuffer.append(entry.getKey()).append("=")
                    .append(URLEncoder.encode(entry.getValue().toString(), encode)).append("&");
        }
        if (stringBuffer.length() > 0) {
            stringBuffer.deleteCharAt(stringBuffer.length() - 1); // 删除最后的一个"&"
        }
        return stringBuffer.toString();
    }

}
