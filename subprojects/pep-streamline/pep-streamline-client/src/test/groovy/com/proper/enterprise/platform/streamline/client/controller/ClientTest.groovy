package com.proper.enterprise.platform.streamline.client.controller

import com.proper.enterprise.platform.streamline.client.StreamlineClient
import com.proper.enterprise.platform.streamline.sdk.status.SignStatus
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Ignore
import org.junit.Test

@Ignore
class ClientTest extends AbstractTest {

    private String serviceKey = "test"

    @Test
    void "addSign"() {
        //新增签名
        assert SignStatus.SUCCESS == new StreamlineClient(serviceKey).addSign("testUser", "test1", "test1").getStatus()
        //重复新增
        assert SignStatus.FAIL == new StreamlineClient(serviceKey).addSign("testUser", "test1", "test1").getStatus()
    }

    @Test
    void "updateSign"() {
        //新增签名
        assert SignStatus.SUCCESS == new StreamlineClient(serviceKey).addSign("test1", "test1", "testUser").getStatus()
        //更新签名
        assert SignStatus.SUCCESS == new StreamlineClient(serviceKey).updateSign("test1", "test1", "testUser").getStatus()
        //更新未注册的签名
        assert SignStatus.FAIL == new StreamlineClient(serviceKey).updateSign("test1", "test1", "testUnregisterUser").getStatus()
    }

    @Test
    void "deleteSign"() {
        //新增签名
        assert SignStatus.SUCCESS == new StreamlineClient(serviceKey).addSign("testUser", "test1", "test1").getStatus()
        //删除签名
        assert SignStatus.SUCCESS == new StreamlineClient(serviceKey).deleteSign("testUser").getStatus()
        //新增签名
        assert SignStatus.SUCCESS == new StreamlineClient(serviceKey).addSign("testUser", "test1", "test1").getStatus()
    }
}
