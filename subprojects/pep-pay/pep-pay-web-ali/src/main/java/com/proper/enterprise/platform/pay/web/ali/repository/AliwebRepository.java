package com.proper.enterprise.platform.pay.web.ali.repository;

import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.pay.web.ali.entity.AliwebEntity;

/**
 * 支付宝网页支付Repository.
 */
public interface AliwebRepository extends BaseRepository<AliwebEntity, String> {

    AliwebEntity findByOutTradeNo(String outTradeNo);

}
