package com.proper.enterprise.platform.pay.web.ali.repository;

import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.pay.web.ali.entity.AliwebEntity;

/**
 * 支付宝网页支付Repository.
 */
public interface AliwebRepository extends BaseRepository<AliwebEntity, String> {

    /**
     * 通过订单号查询支付宝网页支付信息
     *
     * @param outTradeNo 商户内部订单号
     * @return AliwebEntity
     */
    AliwebEntity findByOutTradeNo(String outTradeNo);

}
