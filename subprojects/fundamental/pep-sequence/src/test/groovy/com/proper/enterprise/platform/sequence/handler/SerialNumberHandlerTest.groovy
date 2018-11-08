package com.proper.enterprise.platform.sequence.handler

import com.proper.enterprise.platform.test.AbstractSpringTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

class SerialNumberHandlerTest extends AbstractSpringTest {

    @Autowired
    private SerialNumberHandler handler

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor

    @Test
    void testSerial() {
        def key = 'generatetest1'
        handler.setCurrentId(key, 0)
        def n = 100
        def set = new HashSet<Long>(n)
        def list = new ArrayList<Long>(n)
        n.times {
            Thread thread = new Thread(new Runnable() {
                @Override
                void run() {
                    def nextId = handler.getNextID(key)
                    set.add(nextId)
                    list.add(nextId)
                }
            })
            threadPoolTaskExecutor.execute(thread)
        }
        waitExecutorDone()
        def listSize = list.size()
        def setSize = set.size()
        println "list size: $listSize"
        println " set size: $setSize"
        assert listSize == n
        assert setSize == n
    }

}
