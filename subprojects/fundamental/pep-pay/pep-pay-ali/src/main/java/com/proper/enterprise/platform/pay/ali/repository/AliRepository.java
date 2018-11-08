package com.proper.enterprise.platform.pay.ali.repository;

import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.pay.ali.entity.AliEntity;

/**
 * 支付宝Repository
 */

public interface AliRepository extends BaseRepository<AliEntity, String> {

    /**
     * 通过订单号查询支付宝信息
     *
     * @param outTradeNo 商户内部订单号
     * @return Ali
     */
    AliEntity findByOutTradeNo(String outTradeNo);

    /**
     * 通过订支付宝订单号查询支付宝信息
     *
     * @param tadeNo 支付宝订单号
     * @return Ali
     */
    AliEntity getByTradeNo(String tadeNo);

}
