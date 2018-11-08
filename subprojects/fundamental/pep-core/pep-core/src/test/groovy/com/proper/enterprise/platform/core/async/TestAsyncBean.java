package com.proper.enterprise.platform.core.async;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class TestAsyncBean {
    @Async
    public void testAsync(TestAsync.ThreadValid threadValid) throws InterruptedException {
        Thread.sleep(2 * 1000);
        threadValid.value = Thread.currentThread().getId();
    }
}
