package com.proper.enterprise.platform.pay.ali.repository;


import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.pay.ali.entity.AliRefundEntity;

/**
 * 支付宝退款Repository
 */
public interface AliRefundRepository extends BaseRepository<AliRefundEntity, String> {

    AliRefundEntity findByRefundNo(String refundNo);
}
