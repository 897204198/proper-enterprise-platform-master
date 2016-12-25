package com.proper.enterprise.platform.configs.scheduler

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
    public void scheduleWithTask() {
        def task = new Task()
        def sf = scheduler.scheduleAtFixedRate(task, 50)
        sleep(90)
        sf.cancel(true)
        assert taskCount == 2
    }

    class Task implements Runnable {

        @Override
        void run() {
            taskCount ++
        }
    }

    @Test
    public void scheduleWithAnnotation() {
        // method with @Scheduled will auto run
        assert annoCount > 0
    }

    @Scheduled(fixedDelay = 50L)
    public void addOne() {
        annoCount ++
    }

}
