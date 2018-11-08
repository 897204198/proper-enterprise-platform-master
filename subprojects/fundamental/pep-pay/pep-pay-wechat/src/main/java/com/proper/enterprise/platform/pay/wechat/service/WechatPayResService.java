package com.proper.enterprise.platform.pay.wechat.service;

/**
 * 微信接口响应Service
 */
public interface WechatPayResService {

    /**
     * 向微信服务器发送http请求
     *
     * @param url 请求地址
     * @param beanId 实例BeanId
     * @param requestXML 请求报文
     * @param isHttpsRequest true : http请求 ; false : https请求
     * @param <T> 泛型
     * @return 请求结果
     * @throws Exception
     */
    <T> T getWechatApiRes(String url, String beanId, String requestXML, boolean isHttpsRequest) throws Exception;
}
