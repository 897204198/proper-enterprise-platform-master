package com.proper.enterprise.platform.streamline.client

import com.proper.enterprise.platform.api.auth.service.AccessTokenService
import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.auth.common.jpa.entity.UserEntity
import com.proper.enterprise.platform.auth.common.jpa.repository.AccessTokenRepository
import com.proper.enterprise.platform.core.PEPApplicationContext
import com.proper.enterprise.platform.core.utils.http.HttpClient
import com.proper.enterprise.platform.sys.datadic.entity.DataDicEntity
import com.proper.enterprise.platform.sys.datadic.repository.DataDicRepository
import com.proper.enterprise.platform.sys.datadic.service.DataDicService
import com.proper.enterprise.platform.test.AbstractIntegrationTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class StreamlineAspectTest extends AbstractIntegrationTest {

    @Autowired
    private UserService userService

    @Test
    void testAddUserAOP() {
        String serverUrl = getPrefix()

        String serverToken = 'teststreamline'

        //insert
        def user = new UserEntity()
        user.setUsername('testInsert1')
        user.setPassword('testInsert1')
        user.setEmail('12345678@qq.com')
        user.setPhone('13333333331')
        user.setName('test1')

        userService.save(user)
        Map<String, String> headers = new HashMap<>(1)
        headers.put("X-PEP-TOKEN", serverToken)
        assert "test" == new String(HttpClient.get(serverUrl + "/streamline/testInsert1/testInsert1", headers).getBody())

        def user2 = new UserEntity()
        user2.setUsername('testInsert2')
        user2.setPassword('testInsert2')
        user2.setEmail('12345678@qq.com')
        user2.setPhone('13333333331')
        user2.setName('test2')

        def user3 = new UserEntity()
        user3.setUsername('testInsert3')
        user3.setPassword('testInsert3')
        user3.setEmail('12345678@qq.com')
        user3.setPhone('13333333331')
        user3.setName('test3')

        userService.save(user2, user3)

        assert "test" == new String(HttpClient.get(serverUrl + "/streamline/testInsert2/testInsert2", headers).getBody())
        assert "test" == new String(HttpClient.get(serverUrl + "/streamline/testInsert3/testInsert3", headers).getBody())
    }

    @Test
    void testUpdateUserAOP() {
        String serverUrl = getPrefix()

        String serverToken = 'teststreamline'

        //insert
        def user = new UserEntity()
        user.setUsername('testUpdate1')
        user.setPassword('testUpdate1')
        user.setEmail('12345678@qq.com')
        user.setPhone('13333333331')
        user.setName('test1')

        userService.save(user)
        Map<String, String> headers = new HashMap<>(1)
        headers.put("X-PEP-TOKEN", serverToken)
        assert "test" == new String(HttpClient.get(serverUrl + "/streamline/testUpdate1/testUpdate1", headers).getBody())

        userService.updateChangePassword(user.getId(), 'testUpdate1', 'testUpdate2')
        assert "test" == new String(HttpClient.get(serverUrl + "/streamline/testUpdate1/testUpdate2", headers).getBody())

        def user2 = new UserEntity()
        user2.setUsername('testUpdate2')
        user2.setPassword('testUpdate2')
        user2.setEmail('12345678@qq.com')
        user2.setPhone('13333333331')
        user2.setName('test2')

        def user3 = new UserEntity()
        user3.setUsername('testUpdate3')
        user3.setPassword('testUpdate3')
        user3.setEmail('12345678@qq.com')
        user3.setPhone('13333333331')
        user3.setName('test3')

        userService.save(user2, user3)

        assert "test" == new String(HttpClient.get(serverUrl + "/streamline/testUpdate2/testUpdate2", headers).getBody())
        assert "test" == new String(HttpClient.get(serverUrl + "/streamline/testUpdate3/testUpdate3", headers).getBody())

        Set<String> ids = new HashSet<>()
        ids.add(user2.getId())
        ids.add(user3.getId())
        userService.updateEnable(ids, false)
        assert "test" == new String(HttpClient.get(serverUrl + "/streamline/testUpdate2/testUpdate2", headers).getBody())
        assert "test" == new String(HttpClient.get(serverUrl + "/streamline/testUpdate3/testUpdate3", headers).getBody())
    }

    @Test
    void testDeleteUserAOP() {
        String serverUrl = getPrefix()

        String serverToken = 'teststreamline'

        //insert
        def user = new UserEntity()
        user.setUsername('testDelete1')
        user.setPassword('testDelete1')
        user.setEmail('12345678@qq.com')
        user.setPhone('13333333331')
        user.setName('test1')

        userService.save(user)
        Map<String, String> headers = new HashMap<>(1)
        headers.put("X-PEP-TOKEN", serverToken)
        assert "test" == new String(HttpClient.get(serverUrl + "/streamline/testDelete1/testDelete1", headers).getBody())

        userService.delete(user.getId())
        assert '' == new String(HttpClient.get(serverUrl + "/streamline/testDelete1/testDelete1", headers).getBody())

        def user2 = new UserEntity()
        user2.setUsername('testDelete2')
        user2.setPassword('testDelete2')
        user2.setEmail('12345678@qq.com')
        user2.setPhone('13333333331')
        user2.setName('test2')

        def user3 = new UserEntity()
        user3.setUsername('testDelete3')
        user3.setPassword('testDelete3')
        user3.setEmail('12345678@qq.com')
        user3.setPhone('13333333331')
        user3.setName('test3')

        userService.save(user2, user3)

        assert "test" == new String(HttpClient.get(serverUrl + "/streamline/testDelete2/testDelete2", headers).getBody())
        assert "test" == new String(HttpClient.get(serverUrl + "/streamline/testDelete3/testDelete3", headers).getBody())

        userService.deleteByIds(user2.getId() + "," + user3.getId())
        assert "" == new String(HttpClient.get(serverUrl + "/streamline/testDelete2/testDelete2", headers).getBody())
        assert "" == new String(HttpClient.get(serverUrl + "/streamline/testDelete3/testDelete3", headers).getBody())
    }

    @Before
    void setup() {
        DataDicEntity dataDic = new DataDicEntity()
        dataDic.setName(getPrefix())
        dataDic.setCatalog('STREAMLINE_SERVER')
        dataDic.setCode('URL')
        dataDic.setOrder(1)
        PEPApplicationContext.getBean(DataDicService.class).save(dataDic)

        DataDicEntity dataDic2 = new DataDicEntity()
        dataDic2.setName('teststreamline')
        dataDic2.setCatalog('STREAMLINE_SERVER')
        dataDic2.setCode('TOKEN')
        dataDic2.setOrder(2)
        PEPApplicationContext.getBean(DataDicService.class).save(dataDic2)

        DataDicEntity dataDic3 = new DataDicEntity()
        dataDic3.setName('test')
        dataDic3.setCatalog('STREAMLINE_SERVER')
        dataDic3.setCode('SERVICE_KEY')
        dataDic3.setOrder(2)
        PEPApplicationContext.getBean(DataDicService.class).save(dataDic3)

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
    }
}
