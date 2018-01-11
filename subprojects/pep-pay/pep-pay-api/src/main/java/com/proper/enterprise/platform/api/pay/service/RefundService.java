package com.proper.enterprise.platform.api.pay.service;

import com.proper.enterprise.platform.api.pay.model.RefundInfoReq;

/**
 * 退款Service.
 */
public interface RefundService {

    /**
     * 退款流程.
     *
     * @param req 退款信息请求对象.
     */
    void saveRefundInfoProcess(RefundInfoReq req);

}
