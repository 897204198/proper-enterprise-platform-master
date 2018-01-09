package com.proper.enterprise.platform.pay.proper.service;

import com.proper.enterprise.platform.pay.proper.entity.ProperEntity;
import com.proper.enterprise.platform.pay.proper.entity.ProperRefundEntity;

/**
 * 模拟支付服务类.
 */
public interface ProperPayService {

    /**
     * 保存模拟支付支付信息
     *
     * @param proper 模拟支付对象
     * @return Proper
     */
    ProperEntity save(ProperEntity proper);

    /**
     * 保存模拟支付退款信息
     *
     * @param properRefund 模拟支付退款对象
     * @return ProperRefund
     */
    ProperRefundEntity save(ProperRefundEntity properRefund);

    /**
     * 通过订单号查询模拟支付信息
     *
     * @param outTradeNo 商户内部订单号
     * @return Proper
     */
    ProperEntity findByOutTradeNo(String outTradeNo);

    /**
     * 通过订模拟支付订单号查询模拟支付信息
     *
     * @param tradeNo 模拟支付订单号
     * @return Proper
     */
    ProperEntity getByTradeNo(String tradeNo);

    /**
     * 通过退款单号查询模拟支付退款信息
     *
     * @param refundNo 退款单号
     * @return ProperRefund
     */
    ProperRefundEntity findByRefundNo(String refundNo);
}
