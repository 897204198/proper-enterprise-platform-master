package com.proper.enterprise.platform.api.pay.factory;

import com.proper.enterprise.platform.api.pay.service.NoticeService;
import com.proper.enterprise.platform.api.pay.service.PayService;

/**
 * 支付实例化工厂
 */
public interface PayFactory {

    /**
     * 各个支付方式工厂方法实现类
     *
     * @param payWay 支付方式
     * @param <T> 预支付处理器类型
     * @return 各个支付方式实例service
     */
    <T extends PayService> T newPayService(String payWay);

    /**
     * 各个支付异步通知工厂方法实现类
     *
     * @param payWay 支付方式
     * @param <T> 异步通知处理器类型
     * @return 各个支付方式异步通知实例service
     */
    <T extends NoticeService> T newNoticeService(String payWay);

}
