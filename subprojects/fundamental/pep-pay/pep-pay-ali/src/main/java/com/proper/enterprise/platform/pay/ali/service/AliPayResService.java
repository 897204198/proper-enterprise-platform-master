package com.proper.enterprise.platform.pay.ali.service;

import java.io.IOException;

/**
 * 支付宝统一接口响应Service
 */
public interface AliPayResService {

    /**
     * 支付宝订单查询对象转换
     *
     * @param strRes 返回结果
     * @param responseKey 返回结果键值
     * @param res 对象
     * @return res 对象
     * @throws Exception 转换异常
     */
    Object convertMap2AliPayRes(String strRes, String responseKey, Object res) throws Exception;

    /**
     * 获取远程服务器ATN结果
     *
     * @param urlvalue 指定URL路径地址
     * @return 服务器ATN结果 验证结果集： invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 true
     *         返回正确信息 false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
     * @throws IOException 异常
     */
    String checkUrl(String urlvalue) throws IOException;
}
