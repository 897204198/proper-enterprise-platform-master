package com.proper.enterprise.platform.pay.cmb.service;

import com.proper.enterprise.platform.pay.cmb.document.CmbProtocolDocument;
import com.proper.enterprise.platform.pay.cmb.entity.CmbPayEntity;
import com.proper.enterprise.platform.pay.cmb.entity.CmbRefundEntity;
import com.proper.enterprise.platform.pay.cmb.model.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 一网通支付综合支付Service
 */
public interface CmbPayService {

    /**
     * 获取用户协议信息
     *
     * @param userId
     *        用户ID
     * @return 用户协议信息
     * @throws Exception
     */
    CmbProtocolDocument getUserProtocolInfo(String userId) throws Exception;

    /**
     * 保存用户协议信息
     *
     * @param userProtocolInfo
     *        用户协议信息
     * @throws Exception
     */
    void saveUserProtocolInfo(CmbProtocolDocument userProtocolInfo) throws Exception;

    /**
     * 保存支付结果异步通知结果
     *
     * @param payInfo
     *        支付结果异步通知
     * @throws Exception
     */
    void saveCmbPayNoticeInfo(CmbPayEntity payInfo) throws Exception;

    /**
     * 根据银行通知商户的支付结果消息查询是否已经处理过支付结果异步通知
     *
     * @param msg
     *        支付结果
     * @throws Exception
     */
    CmbPayEntity getPayNoticeInfoByMsg(String msg) throws Exception;

    /**
     * 根据订单号获取一网通订单号及日期
     *
     * @param orderNo 订单号
     * @return 一网通对象
     * @throws Exception
     */
    CmbPayEntity getQueryInfo(String orderNo) throws Exception;

    /**
     * 处理签约协议异步通知
     *
     * @param reqData 请求数据
     * @return 处理结果
     * @throws Exception
     */
    boolean saveNoticeProtocol(String reqData) throws Exception;

    /**
     * 一网通支付结果异步通知验签
     *
     * @param notice 异步通知的字符串
     * @return 验签是否成功
     */
    boolean isValid(String notice);

    /**
     * 取得支付结果异步通知对象
     *
     * @param request
     *        请求
     * @return 支付结果异步通知对象
     * @throws Exception
     */
    CmbPayEntity getCmbPayNoticeInfo(HttpServletRequest request) throws Exception;

    /**
     * 一网通查询单笔交易信息
     *
     * @param orderNo 订单号
     * @return 查询结果
     * @throws Exception
     */
    CmbPayResultRes querySingleOrder(String orderNo) throws Exception;

    /**
     * 通过订单号获取一网通支付结果异步通知信息
     *
     * @param orderNo 订单号
     * @return 支付结果异步通知信息
     * @throws Exception
     */
    CmbPayEntity findByOutTradeNo(String orderNo) throws Exception;

    /**
     * 通过退款单号查询一网通退款信息
     *
     * @param refundNo 退款单号
     * @return WechatRefundInfo
     */
    CmbRefundEntity findByRefundNo(String refundNo);

}
