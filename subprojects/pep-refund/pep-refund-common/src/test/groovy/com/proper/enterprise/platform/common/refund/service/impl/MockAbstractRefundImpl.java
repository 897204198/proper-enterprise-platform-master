package com.proper.enterprise.platform.common.refund.service.impl;

import com.proper.enterprise.platform.api.refund.model.RefundInfoReq;
import com.proper.enterprise.platform.api.refund.model.RefundInfoRes;
import com.proper.enterprise.platform.api.refund.service.RefundService;
import com.proper.enterprise.platform.common.refund.MockRefundInfoRes;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Primary
@Service("refund_business_test")
public class MockAbstractRefundImpl extends AbstractRefundImpl implements RefundService {

    /**
     * 获取退款信息列表.
     *
     * @param req 退款信息请求对象.
     * @return 退款信息列表.
     */
    @Override
    protected  <T extends List<RefundInfoRes>> T getRefundInfo(RefundInfoReq req) {
        List<RefundInfoRes> refundList = new ArrayList<>();
        MockRefundInfoRes refundInfo = new MockRefundInfoRes();
        for(int i = 0; i < 10; i++) {
            refundInfo.setOrderNum("test".concat(String.valueOf(i)));
            refundInfo.setAmout(String.valueOf(i));
            refundList.add(refundInfo);
        }
        //noinspection unchecked
        return (T)refundList;
    }

    /**
     * 校验是否需要退款.
     *
     * @param refundInfo 退款信息.
     * @return 是否需要退款.
     */
    @Override
    protected boolean isNeedRefund(RefundInfoRes refundInfo) {
        return true;
    }

    /**
     * 退款并更新业务信息.
     *
     * @param refundInfo 退款信息.
     */
    @Override
    protected void saveRefund(RefundInfoRes refundInfo) {

    }
}
