package com.proper.enterprise.platform.core.async;

import com.proper.enterprise.platform.test.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestAsync extends AbstractTest {

    @Autowired
    private TestAsyncBean testAsyncBean;

    private volatile ThreadValid threadValid = new ThreadValid();

    public static class ThreadValid {
        long value = 1;
    }

    @Test
    public void testAsync() throws InterruptedException {
        testAsyncBean.testAsync(threadValid);
        Thread.sleep(5 * 1000);
        if (1 == threadValid.value) {
            throw new RuntimeException();
        }
    }

}
