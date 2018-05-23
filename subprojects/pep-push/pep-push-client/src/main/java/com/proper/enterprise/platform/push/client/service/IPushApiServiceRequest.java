package com.proper.enterprise.platform.push.client.service;

import java.util.Map;

/**
 * PushClient Http请求相关的方法封装
 *
 * @author shen
 *
 */
public interface IPushApiServiceRequest {
    /**
     * http请求推送服务器
     *
     * @param baseUrl
     *            推送服务器的基地址
     * @param methodName
     *            调用的方法名称
     * @param params
     *            调有的方法的参数列表
     * @param timeout
     *            超时时间
     * @return http请求返回的结果
     * @throws Exception 请求异常
     */
    String requestServiceServer(final String baseUrl, final String methodName, Map<String, Object> params,
                                int timeout) throws Exception;
}
