package com.proper.enterprise.platform.pay.wechat.repository;

import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.pay.wechat.entity.WechatEntity;

/**
 * 微信Repository
 */
public interface WechatRepository extends BaseRepository<WechatEntity, String> {

    WechatEntity findByOutTradeNo(String outTradeNo);
}
