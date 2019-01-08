package com.proper.enterprise.platform.pay.wechat.service.impl;

import com.proper.enterprise.platform.core.utils.http.HttpClient;
import com.proper.enterprise.platform.core.utils.http.HttpsClient;
import com.proper.enterprise.platform.pay.wechat.PayWechatProperties;
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
@SuppressWarnings("unchecked")
public class WechatPayResServiceImpl implements WechatPayResService {

    @Autowired
    Map<String, Unmarshaller> unmarshallerMap;

    @Autowired
    private PayWechatProperties payWechatProperties;

    /**
     * 向微信服务器发送http请求
     *
     * @param url 请求地址
     * @param beanId 实例Bean
     * @param requestXML 请求报文
     * @param isHttpsRequest true : http请求 ; false : https请求
     * @param <T> 泛型
     * @return 请求结果
     * @throws Exception 请求异常
     */
    @Override
    public <T> T getWechatApiRes(String url, String beanId, String requestXML, boolean isHttpsRequest) throws Exception {
        ResponseEntity<byte[]> response = null;
        // https请求
        if (isHttpsRequest) {
            // 读取证书
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(payWechatProperties.getCertPath());
            // httpsClient
            HttpsClient hc = HttpsClient.withKeyStore(inputStream, "PKCS12", payWechatProperties.getCertPath());
            response = hc.post(url, MediaType.APPLICATION_FORM_URLENCODED, requestXML);
        } else {
            response = HttpClient.post(url, MediaType.APPLICATION_FORM_URLENCODED, requestXML);
        }
        //对账单 获取成功 返回为 文本表格 格式
        String beanIdStr = "unmarshallWechatBillRes";
        int statusCode = 200;
        if (beanIdStr.equals(beanId) && statusCode == response.getStatusCode().value()) {
            return (T)response;
        } else {
            Object res = null;
            byte[] body = response.getBody();
            if (null != body) {
                res = unmarshallerMap.get(beanId).unmarshal(
                    new StreamSource(new ByteArrayInputStream(body)));
            }
            return (T)res;
        }
    }
}
