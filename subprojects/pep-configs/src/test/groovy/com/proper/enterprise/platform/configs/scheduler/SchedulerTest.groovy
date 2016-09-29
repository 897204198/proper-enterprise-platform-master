package com.proper.enterprise.platform.configs.scheduler
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.support.CronTrigger

class SchedulerTest extends AbstractTest {

    def count = 0

    @Autowired
    TaskScheduler scheduler

    @Test
    public void scheduleTest() {
        def task = new Task()
        scheduler.schedule(task, new CronTrigger("* * * * * *"))
        sleep(2000)
        assert count == 2
    }

    class Task implements Runnable {

        @Override
        void run() {
            count ++
        }
    }

}
