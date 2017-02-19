package com.proper.enterprise.platform.pay.cmb.service;

/**
 * 一网通接口响应Service
 */
public interface CmbPayResService {

    /**
     * 向一网通服务器发送http请求
     *
     * @param url 请求地址
     * @param beanId 实例Bean
     * @param requestXML 请求报文
     * @param <T> 泛型
     * @return 请求结果
     * @throws Exception
     */
    <T> T getCmbApiRes(String url, String beanId, String requestXML) throws Exception;
}
