package com.proper.enterprise.platform.pay.cmb.repository;


import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.pay.cmb.entity.CmbPayEntity;

/**
 * 招商银行支付结果异步通知Repository
 */
public interface CmbPayNoticeRepository extends BaseRepository<CmbPayEntity, String> {

    CmbPayEntity findByMsg(String msg);

    CmbPayEntity findByBillNoAndDate(String billNo, String date);

}
