package com.proper.enterprise.platform.auth.common.aop

import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.auth.common.jpa.entity.UserEntity
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class UserChangeAspectTest extends AbstractJPATest {

    public static final Map<String, Boolean> CHANGE_MARK = new HashMap<>(16)

    @Autowired
    private UserService userService

    @Test
    public void testUserChangeAspect() {
        //insert
        def user = new UserEntity()
        user.setUsername('test2')
        user.setPassword('test2')
        user.setEmail('12345678@qq.com')
        user.setPhone('13333333331')
        user.setName('test')

        def user2 = new UserEntity()
        user2.setUsername('test3')
        user2.setPassword('test4')
        user2.setEmail('12345678@qq.com')
        user2.setPhone('13333333332')
        user2.setName('test2')

        userService.save(user, user2)
        assert CHANGE_MARK.get("insertA" + user.getId())
        assert CHANGE_MARK.get("insertA" + user2.getId())

        //update
//        user.setName("update1")
//        userService.update(user)
//        user2.setName("update2")
//        userService.update(user2)
        Set<String> ids = new HashSet<>()
        ids.add(user.getId())
        ids.add(user2.getId())
        userService.updateEnable(ids, false)
        assert CHANGE_MARK.get("updateA" + user.getName())
        assert CHANGE_MARK.get("updateA" + user2.getName())

        //delete
        userService.delete(user.getId())
        userService.delete(user2.getId())
        assert CHANGE_MARK.get("deleteA" + user.getId())
        assert CHANGE_MARK.get("deleteA" + user2.getId())
        CHANGE_MARK.remove("deleteA" + user.getId())
        CHANGE_MARK.remove("deleteA" + user2.getId())

        //batchDelete
        userService.deleteByIds(user.getId() + "," + user2.getId())
        assert CHANGE_MARK.get("deleteA" + user.getId() + "," + user2.getId())
    }
}
