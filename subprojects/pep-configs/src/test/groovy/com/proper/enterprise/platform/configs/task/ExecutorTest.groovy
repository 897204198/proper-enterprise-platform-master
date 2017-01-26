package com.proper.enterprise.platform.configs.task
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.task.TaskExecutor

class ExecutorTest extends AbstractTest {

    def count = 0

    @Autowired
    @Qualifier("pepExecutor")
    TaskExecutor taskExecutor

    @Autowired
    AsyncTask asyncTask

    @Test
    public void useAnnotation() {
        count = 0
        def f = asyncTask.addOne(count)
        assert count == 0
        count = f.get()
        assert count == 1

        taskExecutor.execute(new Task())
        assert count == 1
        waitExecutorDone()
        assert count == 2
    }

    class Task implements Runnable {
        @Override
        void run() {
            sleep(500)
            count ++
        }
    }

}
