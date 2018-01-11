package com.proper.enterprise.platform.pay.cmb.service.impl;

import com.proper.enterprise.platform.core.utils.http.HttpClient;
import com.proper.enterprise.platform.pay.cmb.service.CmbPayResService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Service;

import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.util.Map;

/**
 * 一网通接口响应ServiceImpl
 */
@Service
public class CmbPayResServiceImpl implements CmbPayResService {

    @Autowired
    Map<String, Unmarshaller> unmarshallerMap;

    /**
     * 向一网通服务器发送http请求
     *
     * @param url 请求地址
     * @param beanId 实例Bean
     * @param requestXML 请求报文
     * @param <T> 泛型
     * @return 请求结果
     * @throws Exception 查询异常
     */
    @Override
    public <T> T getCmbApiRes(String url, String beanId, String requestXML) throws Exception {
        // 访问查询订单接口
        ResponseEntity<byte[]> response = HttpClient.get(url.concat("?Request=").concat(requestXML));
        // 获取结果
        Object res = unmarshallerMap.get(beanId).unmarshal(
            new StreamSource(new ByteArrayInputStream(response.getBody())));
        return (T)res;
    }
}
