package com.proper.enterprise.platform.api.pay.service;

/**
 * 异步通知Service
 *
 * @param <T> 各支付平台异步通知实体类
 */
public interface NoticeService<T> {

    /**
     * 异步通知业务处理
     *
     * @param t 处理对象
     */
    void saveNoticeProcess(T t) throws Exception;
}
