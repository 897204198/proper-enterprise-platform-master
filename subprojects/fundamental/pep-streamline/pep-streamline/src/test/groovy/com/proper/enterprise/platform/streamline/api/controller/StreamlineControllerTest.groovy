package com.proper.enterprise.platform.streamline.api.controller

import com.proper.enterprise.platform.api.auth.service.PasswordEncryptService
import com.proper.enterprise.platform.core.i18n.I18NUtil
import com.proper.enterprise.platform.streamline.entity.SignEntity
import com.proper.enterprise.platform.streamline.repository.SignRepository
import com.proper.enterprise.platform.streamline.sdk.constants.StreamlineConstant
import com.proper.enterprise.platform.streamline.sdk.request.SignRequest
import com.proper.enterprise.platform.test.AbstractJPATest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MvcResult

class StreamlineControllerTest extends AbstractJPATest {

    private static final URL = "/streamline"

    @Autowired
    private PasswordEncryptService pwdService

    @Autowired
    private SignRepository signRepository

    @Test
    void "addSign"() {
        //根据用户名和密码注册签名
        SignRequest signRequestParam = new SignRequest()
        //新增必须传业务Id
        assert "addSign can't missing businessId" == post(URL, JSONUtil.toJSON(signRequestParam), HttpStatus.INTERNAL_SERVER_ERROR)
            .getResponse()
            .getContentAsString()
        signRequestParam.setBusinessId("testUser")
        //新增必须传用户名
        assert "addSign can't missing userName" == post(URL, JSONUtil.toJSON(signRequestParam), HttpStatus.INTERNAL_SERVER_ERROR)
            .getResponse()
            .getContentAsString()
        //新增必须传用户密码
        signRequestParam.setUserName("test1")
        assert "addSign can't missing password" == post(URL, JSONUtil.toJSON(signRequestParam), HttpStatus.INTERNAL_SERVER_ERROR)
            .getResponse()
            .getContentAsString()

        //新增必须传serviceKey
        signRequestParam.setPassword(pwdService.encrypt("test1"))
        assert "addSign can't missing serviceKey" == post(URL, JSONUtil.toJSON(signRequestParam), HttpStatus.INTERNAL_SERVER_ERROR)
            .getResponse()
            .getContentAsString()

        //新增成功
        signRequestParam.setServiceKey("test1")
        post(URL, JSONUtil.toJSON(signRequestParam), HttpStatus.CREATED)
        //查询签名
        MvcResult result = get(URL + "/" + signRequestParam.getUserName() + "/" + "test1", HttpStatus.OK)
        assert "test1" == result.getResponse().getContentAsString()
        assert "test1" == result.getResponse().getHeader(StreamlineConstant.SERVICE_KEY)

        //重复注册签名返回异常签名唯一
        assert "userName and Password is unique" == post(URL, JSONUtil.toJSON(signRequestParam), HttpStatus.INTERNAL_SERVER_ERROR)
            .getResponse().getContentAsString()
    }

    @Test
    void "addSigns"() {
        //根据用户名和密码注册签名
        SignRequest signRequestParam = new SignRequest()
        signRequestParam.setBusinessId("testUser")
        signRequestParam.setUserName("test1")
        signRequestParam.setPassword(pwdService.encrypt("test1"))
        def signRequests = [signRequestParam]
        assert "addSign can't missing serviceKey" == post(URL + "/signs", JSONUtil.toJSON(signRequests), HttpStatus.INTERNAL_SERVER_ERROR)
            .getResponse()
            .getContentAsString()

        //新增成功
        signRequestParam.setServiceKey("test1")
        signRequests = [signRequestParam]
        post(URL + "/signs", JSONUtil.toJSON(signRequests), HttpStatus.CREATED)
        //查询签名
        MvcResult result = get(URL + "/" + signRequestParam.getUserName() + "/" + "test1", HttpStatus.OK)
        assert "test1" == result.getResponse().getContentAsString()
        assert "test1" == result.getResponse().getHeader(StreamlineConstant.SERVICE_KEY)

        //重复注册签名返回异常签名唯一
        assert "userName and Password is unique" == post(URL + "/signs", JSONUtil.toJSON(signRequests), HttpStatus.INTERNAL_SERVER_ERROR)
            .getResponse().getContentAsString()
    }

    @Test
    void "delete"() {
        //根据用户名和密码注册签名
        SignRequest signRequestParam = new SignRequest()
        signRequestParam.setBusinessId("testUser")
        signRequestParam.setUserName("test2")
        signRequestParam.setPassword(pwdService.encrypt("test2"))
        signRequestParam.setServiceKey("test2")
        post(URL, JSONUtil.toJSON(signRequestParam), HttpStatus.CREATED)
        //查询签名
        MvcResult result = get(URL + "/" + signRequestParam.getUserName() + "/" + "test2", HttpStatus.OK)
        assert "test2" == result.getResponse().getContentAsString()
        assert "test2" == result.getResponse().getHeader(StreamlineConstant.SERVICE_KEY)
        //根据用户名和密码删除签名
        delete(URL + "/" + signRequestParam.getBusinessId(), HttpStatus.NO_CONTENT)
        //查询签名为空
        assert "" == get(URL + "/" + signRequestParam.getUserName() + "/" + "test2", HttpStatus.OK).getResponse().getContentAsString()
    }

    @Test
    void "deleteSigns"() {
        //根据用户名和密码注册签名
        SignRequest signRequestParam = new SignRequest()
        signRequestParam.setBusinessId("testUser")
        signRequestParam.setUserName("test2")
        signRequestParam.setPassword(pwdService.encrypt("test2"))
        signRequestParam.setServiceKey("test2")

        SignRequest signRequestParam2 = new SignRequest()
        signRequestParam2.setBusinessId("testUser2")
        signRequestParam2.setUserName("test3")
        signRequestParam2.setPassword(pwdService.encrypt("test3"))
        signRequestParam2.setServiceKey("test2")

        def signRequests = [signRequestParam, signRequestParam2]
        post(URL + "/signs", JSONUtil.toJSON(signRequests), HttpStatus.CREATED)
        //查询签名
        MvcResult result = get(URL + "/" + signRequestParam.getUserName() + "/" + "test2", HttpStatus.OK)
        assert "test2" == result.getResponse().getContentAsString()
        assert "test2" == result.getResponse().getHeader(StreamlineConstant.SERVICE_KEY)
        assert "test2" == get(URL + "/" + "test3" + "/" + "test3", HttpStatus.OK).getResponse().getContentAsString()
        //根据用户名和密码删除签名
        delete(URL + "?businessIds=" + signRequestParam.getBusinessId() + "," + signRequestParam2.getBusinessId(), HttpStatus.NO_CONTENT)
        //查询签名为空
        assert "" == get(URL + "/" + signRequestParam.getUserName() + "/" + "test2", HttpStatus.OK).getResponse().getContentAsString()
        assert "" == get(URL + "/" + "test3" + "/" + "test3", HttpStatus.OK).getResponse().getContentAsString()
    }

    @Test
    void "put"() {
        //根据用户名和密码注册签名
        SignRequest signRequestParam = new SignRequest()
        signRequestParam.setBusinessId("testUser")
        signRequestParam.setUserName("test3")
        signRequestParam.setPassword(pwdService.encrypt("test3"))
        signRequestParam.setServiceKey("test3")
        post(URL, JSONUtil.toJSON(signRequestParam), HttpStatus.CREATED)
        //查询签名
        assert "test3" == get(URL + "/" + signRequestParam.getUserName() + "/" + "test3", HttpStatus.OK)
            .getResponse().getContentAsString()
        signRequestParam.setBusinessId("testUnregisteredUser")
        //根据用户Id修改签名
        //用户签名必须存在
        assert "Unregistered user signature" == put(URL, JSONUtil.toJSON(signRequestParam), HttpStatus.INTERNAL_SERVER_ERROR)
            .getResponse().getContentAsString()

        //修改成功
        signRequestParam.setUserName("test4")
        signRequestParam.setPassword(pwdService.encrypt("test4"))
        signRequestParam.setBusinessId("testUser")
        put(URL, JSONUtil.toJSON(signRequestParam), HttpStatus.OK)
        //查询旧签名为空
        assert "" == get(URL + "/test3/test3", HttpStatus.OK).getResponse().getContentAsString()
        //查询新签名找到
        MvcResult result = get(URL + "/" + signRequestParam.getUserName() + "/" + "test4", HttpStatus.OK)
        assert "test3" == result.getResponse().getContentAsString()
        assert "test3" == result.getResponse().getHeader(StreamlineConstant.SERVICE_KEY)
    }

    @Test
    void "putSigns"() {
        //根据用户名和密码注册签名
        SignRequest signRequestParam = new SignRequest()
        signRequestParam.setBusinessId("testUser")
        signRequestParam.setUserName("test2")
        signRequestParam.setPassword(pwdService.encrypt("test2"))
        signRequestParam.setServiceKey("test2")

        SignRequest signRequestParam2 = new SignRequest()
        signRequestParam2.setBusinessId("testUser2")
        signRequestParam2.setUserName("test3")
        signRequestParam2.setPassword(pwdService.encrypt("test3"))
        signRequestParam2.setServiceKey("test2")

        def signRequests = [signRequestParam, signRequestParam2]
        post(URL + "/signs", JSONUtil.toJSON(signRequests), HttpStatus.CREATED)
        //查询签名
        assert "test2" == get(URL + "/" + "test2" + "/" + "test2", HttpStatus.OK)
            .getResponse().getContentAsString()
        signRequestParam.setBusinessId("testUnregisteredUser")
        signRequests = [signRequestParam, signRequestParam2]
        //根据用户Id修改签名
        //用户签名必须存在
        assert "Unregistered user signature" == put(URL + "/signs", JSONUtil.toJSON(signRequests), HttpStatus.INTERNAL_SERVER_ERROR)
            .getResponse().getContentAsString()

        //修改成功
        signRequestParam.setUserName("test4")
        signRequestParam.setPassword(pwdService.encrypt("test4"))
        signRequestParam.setBusinessId("testUser")
        signRequests = [signRequestParam, signRequestParam2]
        put(URL + "/signs", JSONUtil.toJSON(signRequests), HttpStatus.OK)
        //查询旧签名为空
        assert "" == get(URL + "/test2/test2", HttpStatus.OK).getResponse().getContentAsString()
        //查询新签名找到
        MvcResult result = get(URL + "/" + signRequestParam.getUserName() + "/" + "test4", HttpStatus.OK)
        assert "test2" == result.getResponse().getContentAsString()
        assert "test2" == result.getResponse().getHeader(StreamlineConstant.SERVICE_KEY)
    }

    @Test
    void getServiceKeyBySignature() {
        SignEntity signEntity = new SignEntity()
        signEntity.setBusinessId("test")
        signEntity.setSignature("signature")
        signEntity.setServiceKey("testSignature")
        signEntity.setEnable(true)
        signRepository.save(signEntity)
        //查询新签名找到
        MvcResult result = get(URL + "/signature", HttpStatus.OK)
        assert "testSignature" == result.getResponse().getContentAsString()
        assert "testSignature" == result.getResponse().getHeader(StreamlineConstant.SERVICE_KEY)
    }

    @Test
    void validSignature() {

        //查询新签名找到
        MvcResult result = post(URL + "/signature","test", HttpStatus.INTERNAL_SERVER_ERROR)
        assert I18NUtil.getMessage("streamline.valid.signature.fail") == result.getResponse().getContentAsString()
        SignEntity signEntity = new SignEntity()
        signEntity.setBusinessId("test")
        signEntity.setSignature("test")
        signEntity.setServiceKey("testSignature")
        signEntity.setEnable(true)
        signRepository.save(signEntity)
        post(URL + "/signature","test", HttpStatus.CREATED)
    }
}
