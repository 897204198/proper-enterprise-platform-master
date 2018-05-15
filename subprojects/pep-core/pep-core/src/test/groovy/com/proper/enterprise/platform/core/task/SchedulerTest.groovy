package com.proper.enterprise.platform.core.task

import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.annotation.Scheduled

class SchedulerTest extends AbstractTest {

    def taskCount = 0, annoCount = 0

    @Autowired
    TaskScheduler scheduler

    @Test
    void useRunnable() {
        taskCount = 0
        def task = new Task()
        def sf = scheduler.scheduleAtFixedRate(task, 50)
        sleep(500)
        sf.cancel(true)
        assert taskCount >= 2
    }

    class Task implements Runnable {

        @Override
        void run() {
            taskCount ++
        }
    }

    @Test
    void useAnnotation() {
        // method with @Scheduled will auto run
        // but CI may need to sleep some time
        sleep(500)
        assert annoCount > 0
    }

    @Scheduled(fixedDelay = 50L)
    void addOne() {
        annoCount ++
    }

}
