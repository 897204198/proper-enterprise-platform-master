package com.proper.enterprise.platform.streamline.client

import com.proper.enterprise.platform.streamline.sdk.request.SignRequest
import com.proper.enterprise.platform.streamline.sdk.status.SignStatus
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Ignore
import org.junit.Test
import org.springframework.test.context.jdbc.Sql

@Ignore
@Sql
class ClientTest extends AbstractJPATest {

    private String serviceKey = "test"

    @Test
    void "addSign"() {
        //新增签名
        assert SignStatus.SUCCESS == new StreamlineClient(serviceKey).addSign("testUser", "testAdd1", "testAdd1").getStatus()
        //重复新增
        assert SignStatus.FAIL == new StreamlineClient(serviceKey).addSign("testUser", "testAdd1", "testAdd1").getStatus()
    }

    @Test
    void "addSigns"() {
        SignRequest signRequest = new SignRequest()
        signRequest.setBusinessId('testUser2')
        signRequest.setUserName('testAdd2')
        signRequest.setPassword('testAdd2')
        SignRequest signRequest2 = new SignRequest()
        signRequest2.setBusinessId('testUser3')
        signRequest2.setUserName('testAdd3')
        signRequest2.setPassword('testAdd3')
        def signRequests = [signRequest, signRequest2]
        //新增签名
        assert SignStatus.SUCCESS == new StreamlineClient(serviceKey).addSigns(signRequests as Collection<SignRequest>).getStatus()
        //重复新增
        assert SignStatus.FAIL == new StreamlineClient(serviceKey).addSign("testUser2", "testAdd2", "testAdd2").getStatus()
    }

    @Test
    void "updateSign"() {
        //新增签名
        assert SignStatus.SUCCESS == new StreamlineClient(serviceKey).addSign("testUpdateUser", "testUpdate1", "testUpdate1").getStatus()
        //更新签名
        assert SignStatus.SUCCESS == new StreamlineClient(serviceKey).updateSign("testUpdate1", "testUpdate1", "testUpdateUser").getStatus()
        //更新未注册的签名
        assert SignStatus.FAIL == new StreamlineClient(serviceKey).updateSign("testUnregisterUser", "testUnregisterUser", "testUnregisterUser").getStatus()
    }

    @Test
    void "updateSigns"() {
        SignRequest signRequest = new SignRequest()
        signRequest.setBusinessId('testUpdateUser2')
        signRequest.setUserName('testUpdate2')
        signRequest.setPassword('testUpdate2')
        SignRequest signRequest2 = new SignRequest()
        signRequest2.setBusinessId('testUpdateUser3')
        signRequest2.setUserName('testUpdate3')
        signRequest2.setPassword('testUpdate3')
        def signRequests = [signRequest, signRequest2]
        //新增签名
        assert SignStatus.SUCCESS == new StreamlineClient(serviceKey).addSigns(signRequests).getStatus()
        signRequest.setPassword('testUpdate3')
        signRequest2.setPassword('testUpdate2')
        signRequests = [signRequest, signRequest2]
        //更新签名
        assert SignStatus.SUCCESS == new StreamlineClient(serviceKey).updateSigns(signRequests).getStatus()
        //更新未注册的签名
        assert SignStatus.FAIL == new StreamlineClient(serviceKey).updateSign("testUnregisterUser", "testUnregisterUser", "testUnregisterUser").getStatus()
    }

    @Test
    void "deleteSign"() {
        //新增签名
        assert SignStatus.SUCCESS == new StreamlineClient(serviceKey).addSign("testDeleteUser", "testDelete1", "testDelete1").getStatus()
        //删除签名
        assert SignStatus.SUCCESS == new StreamlineClient(serviceKey).deleteSign("testDeleteUser").getStatus()
        //新增签名
        assert SignStatus.SUCCESS == new StreamlineClient(serviceKey).addSign("testDeleteUser", "testDelete1", "testDelete1").getStatus()
        //删除签名
        assert SignStatus.SUCCESS == new StreamlineClient(serviceKey).deleteSigns("testDeleteUser").getStatus()
        //新增签名
        assert SignStatus.SUCCESS == new StreamlineClient(serviceKey).addSign("testDeleteUser", "testDelete1", "testDelete1").getStatus()
    }
}
