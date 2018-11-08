package com.proper.enterprise.platform.common.pay.service.impl;

import com.proper.enterprise.platform.api.pay.model.RefundInfoReq;
import com.proper.enterprise.platform.api.pay.model.RefundInfoRes;
import com.proper.enterprise.platform.api.pay.service.RefundService;

import java.util.List;

/**
 * 退款抽象类.
 */
public abstract class AbstractRefundImpl implements RefundService {

    /**
     * 退款流程.
     *
     * @param req 退款信息请求对象.
     */
    @Override
    public void saveRefundInfoProcess(RefundInfoReq req) {
        // step1 获取退款列表信息
        List<RefundInfoRes> refundInfoList = getRefundInfo(req);
        for (RefundInfoRes refundInfo : refundInfoList) {
            // step2 校验是否需要退款
            if (isNeedRefund(refundInfo)) {
                // step3 退款并更新业务信息
                saveRefund(refundInfo);
            }
        }
    }

    /**
     * 获取退款信息列表.
     *
     * @param req 退款信息请求对象.
     * @return 退款信息列表.
     */
    protected abstract  <T extends List<RefundInfoRes>> T getRefundInfo(RefundInfoReq req);

    /**
     * 校验是否需要退款.
     *
     * @param refundInfo 退款信息.
     * @return 是否需要退款.
     */
    protected abstract boolean isNeedRefund(RefundInfoRes refundInfo);

    /**
     * 退款并更新业务信息.
     *
     * @param refundInfo 退款信息.
     */
    protected abstract void saveRefund(RefundInfoRes refundInfo);

}
