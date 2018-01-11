package com.proper.enterprise.platform.api.pay.service;

/**
 * 异步通知Service
 *
 * @param <T> 各支付平台异步通知实体类
 */
public interface NoticeService<T> {

    /**
     * 异步通知业务处理
     * 为了使异步通知能够尽快得到响应,请各个异步通知实现Service各自加上@Async注解
     *
     * @param t 处理对象
     */
    void saveNoticeProcessAsync(T t) throws Exception;

    /**
     * 判断该订单是否接到了重复的异步通知
     *
     * @param  orderNo 订单号
     * @return true：重复通知，可直接抛弃；false：首次通知，需正常处理
     */
    default boolean isDuplicate(String orderNo) {
        return false;
    }

}
