package com.proper.enterprise.platform.common.pay.service;

import com.proper.enterprise.platform.api.pay.model.OrderReq;
import com.proper.enterprise.platform.api.pay.model.PayResultRes;
import com.proper.enterprise.platform.api.pay.model.PrepayReq;

/**
 * 支付业务接口.
 */
public interface PayBusinessService {

    PayResultRes savePrepayBusiness(String payWay, PrepayReq prepayReq, OrderReq orderReq) throws Exception;
}
