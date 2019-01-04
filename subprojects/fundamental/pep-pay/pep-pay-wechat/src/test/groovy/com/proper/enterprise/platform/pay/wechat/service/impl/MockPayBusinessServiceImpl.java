package com.proper.enterprise.platform.pay.wechat.service.impl;

import com.proper.enterprise.platform.api.pay.model.OrderReq;
import com.proper.enterprise.platform.api.pay.model.PayResultRes;
import com.proper.enterprise.platform.api.pay.model.PrepayReq;
import com.proper.enterprise.platform.common.pay.service.PayBusinessService;
import com.proper.enterprise.platform.pay.wechat.model.WechatOrderReq;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@Primary
public class MockPayBusinessServiceImpl implements PayBusinessService {
    @Override
    public PayResultRes savePrepayBusiness(String payWay, PrepayReq prepayReq, OrderReq orderReq) throws Exception {
        PayResultRes checkRes = new PayResultRes();
        WechatOrderReq wechatOrderReq = (WechatOrderReq) orderReq;
        Assert.isTrue(wechatOrderReq instanceof WechatOrderReq);
        return checkRes;
    }
}
