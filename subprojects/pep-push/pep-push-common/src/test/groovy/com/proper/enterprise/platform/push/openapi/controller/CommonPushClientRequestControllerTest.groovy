package com.proper.enterprise.platform.push.openapi.controller

import com.proper.enterprise.platform.core.utils.StringUtil
import com.proper.enterprise.platform.push.common.db.entity.PushDeviceEntity
import com.proper.enterprise.platform.push.common.db.repository.PushDeviceRepository
import com.proper.enterprise.platform.push.common.db.repository.PushUserRepository
import com.proper.enterprise.platform.push.common.model.PushDevice
import com.proper.enterprise.platform.push.common.model.PushUser
import com.proper.enterprise.platform.push.common.model.enums.PushDeviceType
import com.proper.enterprise.platform.push.common.model.enums.PushMode
import com.proper.enterprise.platform.push.test.PushAbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class CommonPushClientRequestControllerTest extends PushAbstractTest {
    @Autowired
    PushUserRepository userRepo
    @Autowired
    PushDeviceRepository deviceRepo

    private static final String URL_STARTPUSH = "/push/request/startpush"

    @Test
    void startpushTest() {

        Map<String, Object> rtn = null
        Map<String, Object> param = new HashMap<>()

        //参数为空时，没有appkey,返回bad_request
        pushRequest(URL_STARTPUSH, param, HttpStatus.BAD_REQUEST)

        //未注册的appkey,返回错误
        param.put("appkey", "appkey_not_exist")
        rtn = pushRequest(URL_STARTPUSH, param)
        assert rtn['r'] == '1'

        //有appkey且合法时，不会报错
        param.put("appkey", VALID_APPKEY)
        rtn = pushRequest(URL_STARTPUSH, param)
        assert rtn['r'] == '0'

        //非法的deviceType
        param.put("device_type", "invalid_devicetype")
        rtn = pushRequest(URL_STARTPUSH, param)
        assert rtn['r'] == '1'

        //合法deviceType
        param.put("device_type", PushDeviceType.android.toString())
        rtn = pushRequest(URL_STARTPUSH, param)
        assert rtn['r'] == '0'

        //合法的push_mode
        param.put("push_mode", PushMode.huawei)
        rtn = pushRequest(URL_STARTPUSH, param)
        assert rtn['r'] == '0'

        //注册设备,未有push_token
        param.put("deviceid", TEST_DEVICEID1)
        param.put("device_other_info", "test device info")
        pushRequest(URL_STARTPUSH, param)
        PushUser u = userRepo.findByAppkeyAndUserid(VALID_APPKEY, TEST_USERID1)
        assert u == null
        PushDevice d = deviceRepo.findByAppkeyAndDeviceid(VALID_APPKEY, TEST_DEVICEID1)
        assert d.getDeviceid() == TEST_DEVICEID1 && d.getAppkey() == VALID_APPKEY && d.getOtherInfo() == param['device_other_info']
        assert StringUtil.isEmpty(d.getPushToken())
        assert StringUtil.isEmpty(d.getUserid())

        //设备绑定push_token
        param.put("push_token", TEST_PUSHTOKEN1)
        pushRequest(URL_STARTPUSH, param)
        d = deviceRepo.findByAppkeyAndDeviceid(VALID_APPKEY, TEST_DEVICEID1)
        assert StringUtil.isNotEmpty(d.getPushToken()) && d.pushToken == param['push_token']

        //注册用户
        param.put("userid", TEST_USERID1)
        param.put("user_other_info", "test user other info")
        pushRequest(URL_STARTPUSH, param)
        u = userRepo.findByAppkeyAndUserid(VALID_APPKEY, TEST_USERID1)
        assert u.getUserid() == TEST_USERID1 && u.getAppkey() == VALID_APPKEY && u.getOtherInfo() == param['user_other_info']
        d = deviceRepo.findByAppkeyAndDeviceid(VALID_APPKEY, TEST_DEVICEID1)
        assert d.getUserid() == u.getUserid()

        //信息有修改
        param.put("user_other_info", "test user other info2")
        param.put("device_other_info", "test device info2")
        param.put("push_token", TEST_PUSHTOKEN2)
        pushRequest(URL_STARTPUSH, param)
        u = userRepo.findByAppkeyAndUserid(VALID_APPKEY, TEST_USERID1)
        assert u.getOtherInfo() == param['user_other_info']
        d = deviceRepo.findByAppkeyAndDeviceid(VALID_APPKEY, TEST_DEVICEID1)
        assert d.getOtherInfo() == param['device_other_info'] && d.getPushToken() == param['push_token']

        //当user_other_info为空时，不修改用户信息
        param.put("user_other_info", "")
        pushRequest(URL_STARTPUSH, param)
        u = userRepo.findByAppkeyAndUserid(VALID_APPKEY, TEST_USERID1)
        assert u.getOtherInfo() == "test user other info2"

        //pushtoken 被清空
        param.remove("push_token")
        pushRequest(URL_STARTPUSH, param)
        d = deviceRepo.findByAppkeyAndDeviceid(VALID_APPKEY, TEST_DEVICEID1)
        assert StringUtil.isEmpty(d.getPushToken())

        //另一个设备，另一个用户,会创建新的用户和设备信息
        param.put("userid", TEST_USERID2)
        param.put("deviceid", TEST_DEVICEID2)
        pushRequest(URL_STARTPUSH, param)
        assert userRepo.findByAppkeyAndUserid(VALID_APPKEY, TEST_USERID2) != null
        assert deviceRepo.findByAppkeyAndDeviceid(VALID_APPKEY, TEST_DEVICEID2) != null
        assert userRepo.count() == 2
        assert deviceRepo.count() == 2

        //user2登录另一台设备，不解绑已有登录设备
        param.put("deviceid", TEST_DEVICEID3)
        pushRequest(URL_STARTPUSH, param)
        assert deviceRepo.findByAppkeyAndUserid(VALID_APPKEY, TEST_USERID2).size() == 2

        //user2登录另一台设备，解绑已有登录设备
        param.put("deviceid", TEST_DEVICEID2)
        param.put("unbind_other_device", Boolean.TRUE)
        pushRequest(URL_STARTPUSH, param)
        List<PushDeviceEntity> list = deviceRepo.findByAppkeyAndUserid(VALID_APPKEY, TEST_USERID2)
        assert list.size() == 1 && list[0].getUserid() == TEST_USERID2 && list[0].getDeviceid() == TEST_DEVICEID2

        //user1登录user2的当前登录的设备
        param.put("userid", TEST_USERID1)
        pushRequest(URL_STARTPUSH, param)
        list = deviceRepo.findByAppkeyAndUserid(VALID_APPKEY, TEST_USERID1)
        assert list.size() == 1 && list[0].getUserid() == TEST_USERID1 && list[0].getDeviceid() == TEST_DEVICEID2
        d = deviceRepo.findByAppkeyAndDeviceid(VALID_APPKEY, TEST_DEVICEID1)
        assert StringUtil.isEmpty(d.getUserid())  //原设备取消绑定用户


    }

}
