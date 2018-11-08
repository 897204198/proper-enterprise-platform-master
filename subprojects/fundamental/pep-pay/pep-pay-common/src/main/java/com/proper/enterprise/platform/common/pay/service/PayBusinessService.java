package com.proper.enterprise.platform.common.pay.service;

import com.proper.enterprise.platform.api.pay.model.OrderReq;
import com.proper.enterprise.platform.api.pay.model.PayResultRes;
import com.proper.enterprise.platform.api.pay.model.PrepayReq;

/**
 * 支付业务接口.
 */
public interface PayBusinessService {

    /**
     * 预支付业务处理
     *
     * @param payWay 支付方式
     * @param prepayReq 预支付请求对象
     * @param orderReq 请求支付信息参数
     * @return 业务处理结果
     * @throws Exception 异常
     */
    PayResultRes savePrepayBusiness(String payWay, PrepayReq prepayReq, OrderReq orderReq) throws Exception;
}
