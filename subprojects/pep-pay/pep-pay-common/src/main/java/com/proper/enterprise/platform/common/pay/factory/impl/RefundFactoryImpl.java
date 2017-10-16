package com.proper.enterprise.platform.common.pay.factory.impl;

import com.proper.enterprise.platform.api.pay.factory.RefundFactory;
import com.proper.enterprise.platform.api.pay.service.RefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * 退款工厂方法实现类.
 */
@Component
public class RefundFactoryImpl implements RefundFactory {

    @Autowired
    WebApplicationContext wac;

    /**
     * 各个退款业务方法实现类
     *
     * @param business 退款业务
     * @param <T> 退款响应类型
     * @return 各个退款业务实例service
     */
    @Override
    public <T extends RefundService> T newRefundService(String business) {
        //noinspection unchecked
        return (T)wac.getBean("refund_business_".concat(business));
    }

}
