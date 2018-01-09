package com.proper.enterprise.platform.pay.proper.repository;

import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.pay.proper.entity.ProperEntity;

/**
 * 模拟支付Repository.
 */
public interface ProperRepository extends BaseRepository<ProperEntity, String> {

    ProperEntity findByOutTradeNo(String outTradeNo);

    ProperEntity getByTradeNo(String tadeNo);
}
