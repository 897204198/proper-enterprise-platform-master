package com.proper.enterprise.platform.common.pay.service.impl;

import com.proper.enterprise.platform.api.pay.model.OrderReq;
import com.proper.enterprise.platform.api.pay.model.PayResultRes;
import com.proper.enterprise.platform.api.pay.model.PrepayReq;
import com.proper.enterprise.platform.common.pay.service.PayBusinessService;
import org.springframework.stereotype.Service;

/**
 * 支付业务接口实现.
 */
@Service
public class PayBusinessServiceImpl implements PayBusinessService {
    /**
     * 预支付业务处理
     *
     * @return 业务处理结果
     * @throws Exception 异常
     */
    @Override
    public PayResultRes savePrepayBusiness(String payWay, PrepayReq prepayReq, OrderReq orderReq) throws Exception {
        return new PayResultRes();
    }

}
