package com.proper.enterprise.platform.pay.wechat.repository;

import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.pay.wechat.entity.WechatRefundEntity;

/**
 * 微信退款Repository
 */
public interface WechatRefundRepository extends BaseRepository<WechatRefundEntity, String> {

    /**
     * 通过退款单号查询微信退款信息
     *
     * @param refundNo 退款单号
     * @return WechatRefundInfo
     */
    WechatRefundEntity findByRefundNo(String refundNo);
}
