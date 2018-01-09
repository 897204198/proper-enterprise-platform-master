package com.proper.enterprise.platform.pay.proper.repository;

import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.pay.proper.entity.ProperRefundEntity;

/**
 * 模拟退款Repository.
 */
public interface ProperRefundRepository extends BaseRepository<ProperRefundEntity, String> {

    ProperRefundEntity findByRefundNo(String refundNo);
}
