package com.proper.enterprise.platform.pay.web.ali.service;

import java.util.Map;

/**
 * 支付宝网页支付统一接口响应Service.
 */
public interface AliwebPayResService {

    /**
     * 获取支付宝网页支付各个接口的结果.
     *
     * @param req 请求对象.
     * @param res 响应对象.
     * @return res 各个接口响应结果.
     * @throws Exception 异常.
     */
    Object getAliInterfaceRes(Object req, Object res) throws Exception;

    /**
     * 支付宝网页支付验证.
     *
     * @param params 验证参数.
     * @return 是否验证通过.
     * @throws Exception 异常.
     */
    boolean noticeVerify(Map<String, String> params) throws Exception;
}
