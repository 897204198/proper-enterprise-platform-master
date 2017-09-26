package com.proper.enterprise.platform.pay.ali.repository;


import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.pay.ali.entity.AliEntity;

/**
 * 支付宝Repository
 */

public interface AliRepository extends BaseRepository<AliEntity, String> {

    AliEntity findByOutTradeNo(String outTradeNo);

    AliEntity getByTradeNo(String tadeNo);

}
