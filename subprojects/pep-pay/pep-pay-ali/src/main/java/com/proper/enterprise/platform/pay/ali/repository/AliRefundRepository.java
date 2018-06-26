package com.proper.enterprise.platform.pay.ali.repository;

import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.pay.ali.entity.AliRefundEntity;

/**
 * 支付宝退款Repository
 */
public interface AliRefundRepository extends BaseRepository<AliRefundEntity, String> {

    /**
     * 通过退款单号查询支付宝退款信息
     *
     * @param refundNo 退款单号
     * @return AliRefund
     */
    AliRefundEntity findByRefundNo(String refundNo);
}
