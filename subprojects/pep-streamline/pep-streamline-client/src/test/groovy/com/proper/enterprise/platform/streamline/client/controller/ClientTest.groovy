package com.proper.enterprise.platform.streamline.client.controller

import com.proper.enterprise.platform.streamline.client.StreamlineClient
import com.proper.enterprise.platform.streamline.sdk.status.SignStatus
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Ignore
import org.junit.Test

@Ignore
class ClientTest extends AbstractTest {

    private String serviceKey = "test";

    @Test
    void "addSign"() {
        //新增签名
        assert SignStatus.SUCCESS == new StreamlineClient(serviceKey).addSign("test1", "test1").getStatus()
        //重复新增
        assert SignStatus.FAIL == new StreamlineClient(serviceKey).addSign("test1", "test1").getStatus()
    }

    @Test
    void "deleteSign"() {
        //新增签名
        assert SignStatus.SUCCESS == new StreamlineClient(serviceKey).addSign("test1", "test1").getStatus()
        //删除签名
        assert SignStatus.SUCCESS == new StreamlineClient(serviceKey).deleteSign("test1", "test1").getStatus()
        //新增签名
        assert SignStatus.SUCCESS == new StreamlineClient(serviceKey).addSign("test1", "test1").getStatus()
    }
}
