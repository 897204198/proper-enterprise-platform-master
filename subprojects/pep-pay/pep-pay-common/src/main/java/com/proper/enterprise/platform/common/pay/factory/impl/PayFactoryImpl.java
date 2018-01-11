package com.proper.enterprise.platform.common.pay.factory.impl;

import com.proper.enterprise.platform.api.pay.service.NoticeService;
import com.proper.enterprise.platform.api.pay.factory.PayFactory;
import com.proper.enterprise.platform.api.pay.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * 工厂方法实现类
 */
@Component
public class PayFactoryImpl implements PayFactory {

    @Autowired
    WebApplicationContext wac;

    /**
     * 各个支付方式工厂方法实现类
     *
     * @param payWay 支付方式
     * @param <T> 预支付处理器类型
     * @return 各个支付方式实例service
     */
    @Override
    public <T extends PayService> T newPayService(String payWay) {
        return (T)wac.getBean("pay_way_" + payWay);
    }

    /**
     * 各个支付异步通知工厂方法实现类
     *
     * @param payWay 支付方式
     * @param <T> 异步通知处理器类型
     * @return 各个支付方式异步通知实例service
     */
    @Override
    public <T extends NoticeService> T newNoticeService(String payWay) {
        return (T)wac.getBean("pay_notice_" + payWay);
    }
}
