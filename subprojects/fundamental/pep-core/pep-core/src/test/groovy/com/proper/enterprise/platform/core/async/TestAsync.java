package com.proper.enterprise.platform.core.async;

import com.proper.enterprise.platform.test.AbstractSpringTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestAsync extends AbstractSpringTest {

    @Autowired
    private TestAsyncBean testAsyncBean;

    @Autowired
    private TestAsyncService testAsyncService;

    private volatile ThreadValid threadValid = new ThreadValid();

    public static class ThreadValid {
        long value = 1;
    }

    @Test
    public void testAsync() throws InterruptedException {
        testAsyncBean.testAsync(threadValid);
        waitExecutorDone();
        if (1 == threadValid.value) {
            throw new RuntimeException();
        }
    }

    @Test
    public void testServiceAsync() throws InterruptedException {
        testAsyncService.testAsync(threadValid);
        waitExecutorDone();
        if (1 == threadValid.value) {
            throw new RuntimeException();
        }
    }
}
