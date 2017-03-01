package com.proper.enterprise.platform.pay.wechat.service.impl;

import com.proper.enterprise.platform.core.utils.http.HttpClient;
import com.proper.enterprise.platform.core.utils.http.HttpsClient;
import com.proper.enterprise.platform.pay.wechat.constants.WechatConstants;
import com.proper.enterprise.platform.pay.wechat.service.WechatPayResService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Service;

import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * 微信接口响应ServiceImpl
 */
@Service
public class WechatPayResServiceImpl implements WechatPayResService {

    @Autowired
    Map<String, Unmarshaller> unmarshallerMap;

    /**
     * 向微信服务器发送http请求
     *
     * @param url 请求地址
     * @param beanId 实例Bean
     * @param requestXML 请求报文
     * @param isHttpsRequest true : http请求 ; false : https请求
     * @param <T> 泛型
     * @return 请求结果
     * @throws Exception
     */
    @Override
    public <T> T getWechatApiRes(String url, String beanId, String requestXML, boolean isHttpsRequest) throws Exception {
        ResponseEntity<byte[]> response = null;
        // https请求
        if(isHttpsRequest) {
            // 读取证书
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(WechatConstants.WECHAT_PAY_CERT_PATH);
            // httpsClient
            HttpsClient hc = HttpsClient.withKeyStore(inputStream, "PKCS12", WechatConstants.WECHAT_PAY_MCHID);
            response = hc.post(url, MediaType.APPLICATION_FORM_URLENCODED, requestXML);
        } else {
            response = HttpClient.post(url, MediaType.APPLICATION_FORM_URLENCODED, requestXML);
        }
        Object res = unmarshallerMap.get(beanId).unmarshal(
            new StreamSource(new ByteArrayInputStream(response.getBody())));
        return (T)res;
    }
}
