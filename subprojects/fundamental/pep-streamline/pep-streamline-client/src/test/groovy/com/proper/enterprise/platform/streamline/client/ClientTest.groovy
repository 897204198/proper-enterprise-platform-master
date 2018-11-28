package com.proper.enterprise.platform.streamline.client

import com.proper.enterprise.platform.api.auth.service.AccessTokenService
import com.proper.enterprise.platform.auth.common.jpa.repository.AccessTokenRepository
import com.proper.enterprise.platform.core.PEPApplicationContext
import com.proper.enterprise.platform.streamline.repository.SignRepository
import com.proper.enterprise.platform.streamline.sdk.request.SignRequest
import com.proper.enterprise.platform.streamline.sdk.status.SignStatus
import com.proper.enterprise.platform.sys.datadic.entity.DataDicEntity
import com.proper.enterprise.platform.sys.datadic.repository.DataDicRepository
import com.proper.enterprise.platform.sys.datadic.service.DataDicService
import com.proper.enterprise.platform.test.AbstractIntegrationTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.test.context.jdbc.Sql

@Sql
class ClientTest extends AbstractIntegrationTest {

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

    @Before
    void setup() {
        DataDicEntity dataDic = new DataDicEntity()
        dataDic.setId('streamline_server_url')
        dataDic.setName(getPrefix())
        dataDic.setCatalog('STREAMLINE_SERVER')
        dataDic.setCode('URL')
        PEPApplicationContext.getBean(DataDicService.class).update(dataDic)

        DataDicEntity dataDic2 = new DataDicEntity()
        dataDic2.setId('streamline_server_token')
        dataDic2.setName('teststreamline')
        dataDic2.setCatalog('STREAMLINE_SERVER')
        dataDic2.setCode('TOKEN')
        PEPApplicationContext.getBean(DataDicService.class).update(dataDic2)

        MockAccessToken accessToken = new MockAccessToken()
        accessToken.setName('streamline')
        accessToken.setResourcesDescription('*:/streamline/**')
        accessToken.setToken('teststreamline')
        accessToken.setUserId('streamline')
        PEPApplicationContext.getBean(AccessTokenService.class).saveOrUpdate(accessToken)
    }

    @After
    void teardown() {
        PEPApplicationContext.getBean(DataDicRepository.class).deleteAll()
        PEPApplicationContext.getBean(AccessTokenRepository.class).deleteAll()
        PEPApplicationContext.getBean(SignRepository.class).deleteAll()
    }
}
