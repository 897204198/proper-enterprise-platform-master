package com.proper.enterprise.platform.pay.wechat.repository;

import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.pay.wechat.entity.WechatRefundEntity;

/**
 * 微信退款Repository
 */
public interface WechatRefundRepository extends BaseRepository<WechatRefundEntity, String> {

    WechatRefundEntity findByRefundNo(String refundNo);
}
