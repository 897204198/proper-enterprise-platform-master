package com.proper.enterprise.platform.auth.service.impl
import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.auth.common.entity.UserEntity
import com.proper.enterprise.platform.auth.jwt.model.JWTHeader
import com.proper.enterprise.platform.auth.jwt.model.impl.JWTPayloadImpl
import com.proper.enterprise.platform.auth.jwt.service.JWTService
import com.proper.enterprise.platform.core.utils.ConfCenter
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.annotation.NoTx
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql

class UserServiceImplTest extends AbstractTest {

    @Autowired
    UserService userService

    @Autowired
    JWTService jwtService

    def static final DEFAULT_USER = ConfCenter.get("auth.historical.defaultUserId", "PEP_SYS")

    @Test
    @Sql
    public void getUserFromToken() {
        def token = jwtService.generateToken(new JWTHeader('uid', 'unm'), new JWTPayloadImpl())
        mockRequest.addHeader('Authorization', token)

        def user = userService.save(new UserEntity('a', 'b'))
        assert user.getCreateUserId() == 'uid'
    }

    @Test
    @NoTx
    public void fallbackDefaultUserWithoutToken() {
        def user = userService.save(new UserEntity('a', 'b'))
        assert user.getCreateUserId() == DEFAULT_USER
    }

    @Test
    public void fallbackDefaultUserUsingFakeToken() {
        mockRequest.addHeader('Authorization', 'a.b.c')
        def user = userService.save(new UserEntity('a', 'b'))
        assert user.getCreateUserId() == DEFAULT_USER
    }

}
