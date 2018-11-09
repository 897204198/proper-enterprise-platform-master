package com.proper.enterprise.platform.core.async;

import org.springframework.scheduling.annotation.Async;

public interface TestAsyncService {


    /**
     * 异步接口测试
     *
     * @param threadValid 线程安全参数 判断是否新起一个线程
     * @throws InterruptedException 中断异常
     */
    @Async
    void testAsync(TestAsync.ThreadValid threadValid) throws InterruptedException;
}
