package com.proper.enterprise.platform.core.async;

import org.springframework.stereotype.Service;

@Service
public class TestAsyncServiceImpl implements TestAsyncService {
    @Override
    public void testAsync(TestAsync.ThreadValid threadValid) throws InterruptedException {
        Thread.sleep(2 * 1000);
        threadValid.value = Thread.currentThread().getId();
    }
}
