package com.proper.enterprise.platform.pay.wechat.repository;

import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.pay.wechat.entity.WechatEntity;

/**
 * 微信Repository
 */
public interface WechatRepository extends BaseRepository<WechatEntity, String> {

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
     * @param transactionId 微信订单号
     * @return WechatInfo
     */
    WechatEntity getByTransactionId(String transactionId);
}
