package com.proper.enterprise.platform.api.pay.service;

import com.proper.enterprise.platform.api.pay.model.*;

/**
 * 支付Service
 */
public interface PayService {

    /**
     * 预支付业务处理
     *
     * @return 业务处理结果
     * @throws Exception 异常
     */
    PayResultRes savePrepayBusiness(String payWay, PrepayReq prepayReq, OrderReq orderReq) throws Exception;

    /**
     * 预支付请求
     *
     * @param req 预支付请求对象
     * @return 预支付请求处理结果对象
     */
    <T extends PayResultRes> T savePrepay(PrepayReq req);

    /**
     * 查询订单状态
     *
     * @param outTradeNo 订单号(系统内)
     * @return 查询结果
     */
    <T> T queryPay(String outTradeNo);

    /**
     * 退款
     *
     * @param refundReq 退款请求对象
     * @return 退款结果
     */
    <T> T  refundPay(RefundReq refundReq);

    /**
     * 退款查询
     *
     * @param orderNo 原订单号
     * @param refundNo 退款单号
     * @return 查询结果
     */
    <T> T  queryRefund(String orderNo, String refundNo);

    /**
     * 获取对账单
     * @param billReq
     * @param <T>
     * @return
     */
    <T> T getBill(BillReq billReq);

}
