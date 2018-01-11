package com.proper.enterprise.platform.pay.cmb.repository;

import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.pay.cmb.entity.CmbRefundEntity;

/**
 * 招商银行退款Repository
 */
public interface CmbRefundRepository extends BaseRepository<CmbRefundEntity, String> {

    CmbRefundEntity findByRefundNo(String refundNo);
}
