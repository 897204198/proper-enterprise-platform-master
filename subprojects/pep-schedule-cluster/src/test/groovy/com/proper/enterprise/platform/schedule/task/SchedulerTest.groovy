package com.proper.enterprise.platform.schedule.task

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
        scheduler.scheduleAtFixedRate(task, 50)
        sleep(800)
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
        while (annoCount == 0) {
            println "sleep 5 milliseconds to wait @Scheduled task run"
            sleep(5)
        }
        assert annoCount > 0
    }


    @Scheduled(fixedDelay = 50L)
    void addOne() {
        annoCount ++
    }

}
