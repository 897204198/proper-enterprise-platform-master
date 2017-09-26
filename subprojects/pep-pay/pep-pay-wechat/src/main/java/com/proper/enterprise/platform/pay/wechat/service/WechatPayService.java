package com.proper.enterprise.platform.pay.wechat.service;

import com.proper.enterprise.platform.pay.wechat.entity.WechatEntity;
import com.proper.enterprise.platform.pay.wechat.entity.WechatRefundEntity;
import com.proper.enterprise.platform.pay.wechat.model.*;

/**
 * 微信支付服务接口
 *
 */
public interface WechatPayService {
    /**
     * 保存微信支付信息
     *
     * @param wechatInfo 微信支付对象
     * @return wechatInfo
     */
    WechatEntity save(WechatEntity wechatInfo);

    /**
     * 保存微信退款信息
     *
     * @param weixinRefundInfo 微信退款对象
     * @return weixinRefundInfo
     */
    WechatRefundEntity save(WechatRefundEntity weixinRefundInfo);

    /**
     * 通过订单号查询微信支付信息
     *
     * @param outTradeNo 商户内部订单号
     * @return WechatInfo
     */
    WechatEntity findByOutTradeNo(String outTradeNo);

    /**
     * 通过微信订单号查询微信支付信息
     *
     * @param tradeNo 微信订单号
     * @return WechatInfo
     */
    WechatEntity getByTradeNo(String tradeNo);

    /**
     * 通过退款单号查询微信退款信息
     *
     * @param refundNo 退款单号
     * @return WechatRefundInfo
     */
    WechatRefundEntity findByRefundNo(String refundNo);

    /**
     * 创建微信支付信息Entity
     *
     * @param wechatNoticeInfo 微信异步通知对象
     * @return wechatInfo 转换后的微信异步通知支付信息
     */
    WechatEntity getWechatNoticeInfo(WechatNoticeRes wechatNoticeInfo);

    /**
     * 微信异步通知验签
     *
     * @param noticeRes 异步通知对象
     * @return 验签结果
     */
    boolean isValid(WechatNoticeRes noticeRes);
}
