package com.proper.enterprise.platform.pay.proper.repository;

import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.pay.proper.entity.ProperRefundEntity;

/**
 * 模拟退款Repository.
 */
public interface ProperRefundRepository extends BaseRepository<ProperRefundEntity, String> {

    /**
     * 通过退款单号查询模拟支付退款信息
     *
     * @param refundNo 退款单号
     * @return ProperRefund
     */
    ProperRefundEntity findByRefundNo(String refundNo);
}
