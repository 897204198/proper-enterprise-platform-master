package com.proper.enterprise.platform.pay.ali.service.impl;

import com.proper.enterprise.platform.api.pay.model.OrderReq;
import com.proper.enterprise.platform.api.pay.model.PayResultRes;
import com.proper.enterprise.platform.api.pay.model.PrepayReq;
import com.proper.enterprise.platform.common.pay.service.PayBusinessService;
import com.proper.enterprise.platform.pay.ali.model.AliOrderReq;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@Primary
public class MockPayBusinessServiceImpl implements PayBusinessService {
    @Override
    public PayResultRes savePrepayBusiness(String payWay, PrepayReq prepayReq, OrderReq orderReq) throws Exception {
        PayResultRes checkRes = new PayResultRes();
        AliOrderReq aliOrderReq = (AliOrderReq) orderReq;
        Assert.isTrue(aliOrderReq instanceof AliOrderReq);
        return checkRes;
    }
}
