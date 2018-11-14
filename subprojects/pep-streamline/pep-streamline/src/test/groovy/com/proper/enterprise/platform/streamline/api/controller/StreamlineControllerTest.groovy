package com.proper.enterprise.platform.streamline.api.controller

import com.proper.enterprise.platform.streamline.sdk.request.SignRequest
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Ignore
import org.junit.Test
import org.springframework.http.HttpStatus
import com.proper.enterprise.platform.test.utils.JSONUtil

@Ignore
class StreamlineControllerTest extends AbstractTest {

    private static final URL = "/streamline"

    @Test
    void "addSign"() {
        //根据用户名和密码注册签名
        SignRequest signRequestParam = new SignRequest()
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
        signRequestParam.setPassword("test1")
        assert "addSign can't missing serviceKey" == post(URL, JSONUtil.toJSON(signRequestParam), HttpStatus.INTERNAL_SERVER_ERROR)
            .getResponse()
            .getContentAsString()

        //新增成功
        signRequestParam.setServiceKey("test1")
        post(URL, JSONUtil.toJSON(signRequestParam), HttpStatus.CREATED)
        //查询签名
        assert "test1" == get(URL + "/" + signRequestParam.getUserName() + "/" + signRequestParam.getPassword(), HttpStatus.OK)
            .getResponse().getContentAsString()

        //重复注册签名返回异常签名唯一
        assert "userName and Password is unique" == post(URL, JSONUtil.toJSON(signRequestParam), HttpStatus.INTERNAL_SERVER_ERROR)
            .getResponse().getContentAsString()
    }

    @Test
    void "delete"() {
        //根据用户名和密码注册签名
        SignRequest signRequestParam = new SignRequest()
        signRequestParam.setUserName("test2")
        signRequestParam.setPassword("test2")
        signRequestParam.setServiceKey("test2")
        post(URL, JSONUtil.toJSON(signRequestParam), HttpStatus.CREATED)
        //查询签名
        assert "test2" == get(URL + "/" + signRequestParam.getUserName() + "/" + signRequestParam.getPassword(), HttpStatus.OK)
            .getResponse().getContentAsString()
        //根据用户名和密码删除签名
        delete(URL + "/" + signRequestParam.getUserName() + "/" + signRequestParam.getPassword(), HttpStatus.NO_CONTENT)
        //查询签名404
        get(URL + "/" + signRequestParam.getUserName() + "/" + signRequestParam.getPassword(), HttpStatus.NOT_FOUND)
    }

    @Test
    void "put"() {
        //根据用户名和密码注册签名
        SignRequest signRequestParam = new SignRequest()
        signRequestParam.setUserName("test3")
        signRequestParam.setPassword("test3")
        signRequestParam.setServiceKey("test3")
        post(URL, JSONUtil.toJSON(signRequestParam), HttpStatus.CREATED)
        //查询签名
        assert "test3" == get(URL + "/" + signRequestParam.getUserName() + "/" + signRequestParam.getPassword(), HttpStatus.OK)
            .getResponse().getContentAsString()
        //根据用户名和密码修改签名
        //旧用户名密码必须匹配
        assert "The old username and password do not match" == put(URL, JSONUtil.toJSON(signRequestParam), HttpStatus.INTERNAL_SERVER_ERROR)
            .getResponse().getContentAsString()

        //修改成功
        signRequestParam.setUserName("test4")
        signRequestParam.setPassword("test4")
        signRequestParam.setOldUserName("test3")
        signRequestParam.setOldPassword("test3")
        put(URL, JSONUtil.toJSON(signRequestParam), HttpStatus.OK)
        //查询旧签名404
        get(URL + "/test3/test3", HttpStatus.NOT_FOUND)
        //查询新签名找到
        assert "test3" == get(URL + "/" + signRequestParam.getUserName() + "/" + signRequestParam.getPassword(), HttpStatus.OK)
            .getResponse().getContentAsString()
    }
}
