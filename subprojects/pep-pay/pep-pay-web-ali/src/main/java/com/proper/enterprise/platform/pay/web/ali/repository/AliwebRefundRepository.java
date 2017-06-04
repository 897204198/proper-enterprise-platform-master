package com.proper.enterprise.platform.pay.web.ali.repository;

import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.pay.web.ali.entity.AliwebRefundEntity;

/**
 * 支付宝网页支付退款Repository.
 */
public interface AliwebRefundRepository extends BaseRepository<AliwebRefundEntity, String> {

    AliwebRefundEntity findByRefundNo(String refundNo);

}
