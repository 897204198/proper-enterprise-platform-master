package com.proper.enterprise.platform.pay.proper.repository;

import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.pay.proper.entity.ProperEntity;

/**
 * 模拟支付Repository.
 */
public interface ProperRepository extends BaseRepository<ProperEntity, String> {

    /**
     * 通过订单号查询模拟支付信息
     *
     * @param outTradeNo 商户内部订单号
     * @return Proper
     */
    ProperEntity findByOutTradeNo(String outTradeNo);

    /**
     * 通过模拟支付订单号查询模拟支付信息
     *
     * @param tadeNo 模拟支付订单号
     * @return Proper
     */
    ProperEntity getByTradeNo(String tadeNo);
}
