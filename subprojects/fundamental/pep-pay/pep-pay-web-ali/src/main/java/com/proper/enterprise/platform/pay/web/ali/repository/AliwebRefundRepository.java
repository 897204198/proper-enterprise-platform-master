package com.proper.enterprise.platform.pay.web.ali.repository;

import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.pay.web.ali.entity.AliwebRefundEntity;

/**
 * 支付宝网页支付退款Repository.
 */
public interface AliwebRefundRepository extends BaseRepository<AliwebRefundEntity, String> {

    /**
     * 通过退款单号查询支付宝退款信息
     *
     * @param refundNo 退款单号
     * @return AliRefund
     */
    AliwebRefundEntity findByRefundNo(String refundNo);

}
