package com.proper.enterprise.platform.oopsearch.sync.mysql.listener

import com.github.shyiko.mysql.binlog.BinaryLogClient
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test

class BinlogLifecycleListenerTest extends AbstractTest{

    @Test
    void test(){
        BinlogLifecycleListener binlogLifecycleListener = new BinlogLifecycleListener()
        BinaryLogClient binaryLogClient = new BinaryLogClient("localhost", 3307, "root", "mysql")
        binlogLifecycleListener.onConnect(binaryLogClient)
        binlogLifecycleListener.onCommunicationFailure(binaryLogClient, new Exception())
        binlogLifecycleListener.onEventDeserializationFailure(binaryLogClient, new Exception())
        binlogLifecycleListener.onDisconnect(binaryLogClient)
    }
}
