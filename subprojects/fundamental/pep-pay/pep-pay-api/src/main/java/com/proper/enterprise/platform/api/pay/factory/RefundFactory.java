package com.proper.enterprise.platform.api.pay.factory;

import com.proper.enterprise.platform.api.pay.service.RefundService;

/**
 * 退款工厂接口.
 */
public interface RefundFactory {

    /**
     * 各个退款业务方法实现类
     *
     * @param business 退款业务
     * @param <T> 退款响应类型
     * @return 各个退款业务实例service
     */
    <T extends RefundService> T newRefundService(String business);
}
