package com.proper.enterprise.platform.pay.web.ali.service;

import com.proper.enterprise.platform.pay.web.ali.entity.AliwebEntity;
import com.proper.enterprise.platform.pay.web.ali.entity.AliwebRefundEntity;

import java.util.Map;

/**
 * 支付宝web支付Service.
 */
public interface AliwebPayService {

    /**
     * 保存支付宝网页支付信息
     *
     * @param ali 支付宝网页支付对象
     * @return AliwebEntity
     */
    AliwebEntity save(AliwebEntity ali);

    /**
     * 保存支付宝网页支付退款信息
     *
     * @param aliRefund 支付宝网页支付退款对象
     * @return AliRefund
     */
    AliwebRefundEntity save(AliwebRefundEntity aliRefund);

    /**
     * 通过订单号查询支付宝网页支付信息
     *
     * @param outTradeNo 商户内部订单号
     * @return AliwebEntity
     */
    AliwebEntity findByOutTradeNo(String outTradeNo);

    /**
     * 通过退款单号查询支付宝退款信息
     *
     * @param refundNo 退款单号
     * @return AliRefund
     */
    AliwebRefundEntity findByRefundNo(String refundNo);

    /**
     * 创建支付宝网页支付信息Entity
     *
     * @param params 参数
     * @return alipayinfo 支付信息
     * @throws Exception
     */
    AliwebEntity getAliwebNoticeInfo(Map<String, String> params) throws Exception;
}
