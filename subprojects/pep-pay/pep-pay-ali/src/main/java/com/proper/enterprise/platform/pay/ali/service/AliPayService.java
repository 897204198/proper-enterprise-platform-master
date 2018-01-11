package com.proper.enterprise.platform.pay.ali.service;

import com.proper.enterprise.platform.pay.ali.entity.AliEntity;
import com.proper.enterprise.platform.pay.ali.entity.AliRefundEntity;

import java.util.Map;

/**
 * 支付宝综合支付Service
 */
public interface AliPayService {

    /**
     * 保存支付宝支付信息
     *
     * @param ali 支付宝对象
     * @return Ali
     */
    AliEntity save(AliEntity ali);

    /**
     * 保存支付宝退款信息
     *
     * @param aliRefund 支付宝退款对象
     * @return AliRefund
     */
    AliRefundEntity save(AliRefundEntity aliRefund);

    /**
     * 通过订单号查询支付宝信息
     *
     * @param outTradeNo 商户内部订单号
     * @return Ali
     */
    AliEntity findByOutTradeNo(String outTradeNo);

    /**
     * 通过订支付宝订单号查询支付宝信息
     *
     * @param tradeNo 支付宝订单号
     * @return Ali
     */
    AliEntity getByTradeNo(String tradeNo);

    /**
     * 通过退款单号查询支付宝退款信息
     *
     * @param refundNo 退款单号
     * @return AliRefund
     */
    AliRefundEntity findByRefundNo(String refundNo);

    /**
     * 验证消息是否是支付宝发出的合法消息
     *
     * @param params 通知返回来的参数数组
     * @return 验证结果
     */
    boolean verify(Map<String, String> params) throws Exception;

    /**
     * 创建支付宝支付信息Entity
     *
     * @param params 参数
     * @return alipayinfo 支付信息
     * @throws Exception 参数获取异常
     */
    AliEntity getAliNoticeInfo(Map<String, String> params) throws Exception;

    /**
     * 创建订单信息
     *
     * @param t 对象
     * @param clz 对象class
     * @param <T> 泛型
     * @return 结果
     * @throws Exception
     */
    <T> String getOrderInfo(T t, Class<T> clz) throws Exception;

}
