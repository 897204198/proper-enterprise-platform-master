package com.proper.enterprise.platform.auth.streamline

import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.auth.common.jpa.entity.UserEntity
import com.proper.enterprise.platform.core.utils.http.HttpClient
import com.proper.enterprise.platform.sys.datadic.util.DataDicUtil
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Ignore
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

@Ignore
class StreamlineAspectTest extends AbstractJPATest {

    @Autowired
    private UserService userService

    private String serverToken = 'testToken'

    @Test
    void testAddUserAOP() {
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
        assert DataDicUtil.get("STREAMLINE_SERVER", "SERVICE_KEY").getName() == new String(HttpClient
            .get(DataDicUtil.get("STREAMLINE_SERVER", "URL").getName() + "/streamline/testInsert1/testInsert1", headers).getBody())

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

        assert DataDicUtil.get("STREAMLINE_SERVER", "SERVICE_KEY").getName() == new String(HttpClient.get(DataDicUtil.get("STREAMLINE_SERVER", "URL").getName() + "/streamline/testInsert2/testInsert2", headers).getBody())
        assert DataDicUtil.get("STREAMLINE_SERVER", "SERVICE_KEY").getName() == new String(HttpClient.get(DataDicUtil.get("STREAMLINE_SERVER", "URL").getName() + "/streamline/testInsert3/testInsert3", headers).getBody())
    }

    @Test
    void testUpdateUserAOP() {
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
        assert DataDicUtil.get("STREAMLINE_SERVER", "SERVICE_KEY").getName() == new String(HttpClient.get(DataDicUtil.get("STREAMLINE_SERVER", "URL").getName() + "/streamline/testUpdate1/testUpdate1", headers).getBody())

        userService.updateChangePassword(user.getId(), 'testUpdate1', 'testUpdate2')
        assert DataDicUtil.get("STREAMLINE_SERVER", "SERVICE_KEY").getName() == new String(HttpClient.get(DataDicUtil.get("STREAMLINE_SERVER", "URL").getName() + "/streamline/testUpdate1/testUpdate2", headers).getBody())

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

        assert DataDicUtil.get("STREAMLINE_SERVER", "SERVICE_KEY").getName() == new String(HttpClient.get(DataDicUtil.get("STREAMLINE_SERVER", "URL").getName() + "/streamline/testUpdate2/testUpdate2", headers).getBody())
        assert DataDicUtil.get("STREAMLINE_SERVER", "SERVICE_KEY").getName() == new String(HttpClient.get(DataDicUtil.get("STREAMLINE_SERVER", "URL").getName() + "/streamline/testUpdate3/testUpdate3", headers).getBody())

        Set<String> ids = new HashSet<>()
        ids.add(user2.getId())
        ids.add(user3.getId())
        userService.updateEnable(ids, false)
        assert DataDicUtil.get("STREAMLINE_SERVER", "SERVICE_KEY").getName() == new String(HttpClient.get(DataDicUtil.get("STREAMLINE_SERVER", "URL").getName() + "/streamline/testUpdate2/testUpdate2", headers).getBody())
        assert DataDicUtil.get("STREAMLINE_SERVER", "SERVICE_KEY").getName() == new String(HttpClient.get(DataDicUtil.get("STREAMLINE_SERVER", "URL").getName() + "/streamline/testUpdate3/testUpdate3", headers).getBody())
    }

    @Test
    void testDeleteUserAOP() {
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
        assert DataDicUtil.get("STREAMLINE_SERVER", "SERVICE_KEY").getName() == new String(HttpClient.get(DataDicUtil.get("STREAMLINE_SERVER", "URL").getName() + "/streamline/testDelete1/testDelete1", headers).getBody())

        userService.delete(user.getId())
        assert HttpStatus.NOT_FOUND == HttpClient.get(DataDicUtil.get("STREAMLINE_SERVER", "URL").getName() + "/streamline/testDelete1/testDelete1", headers).getStatusCode()

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

        assert DataDicUtil.get("STREAMLINE_SERVER", "SERVICE_KEY").getName() == new String(HttpClient.get(DataDicUtil.get("STREAMLINE_SERVER", "URL").getName() + "/streamline/testDelete2/testDelete2", headers).getBody())
        assert DataDicUtil.get("STREAMLINE_SERVER", "SERVICE_KEY").getName() == new String(HttpClient.get(DataDicUtil.get("STREAMLINE_SERVER", "URL").getName() + "/streamline/testDelete3/testDelete3", headers).getBody())

        userService.deleteByIds(user2.getId() + "," + user3.getId())
        assert HttpStatus.NOT_FOUND == HttpClient.get(DataDicUtil.get("STREAMLINE_SERVER", "URL").getName() + "/streamline/testDelete2/testDelete2", headers).getStatusCode()
        assert HttpStatus.NOT_FOUND == HttpClient.get(DataDicUtil.get("STREAMLINE_SERVER", "URL").getName() + "/streamline/testDelete3/testDelete3", headers).getStatusCode()
    }
}
