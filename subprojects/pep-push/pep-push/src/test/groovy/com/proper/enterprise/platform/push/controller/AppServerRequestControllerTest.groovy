package com.proper.enterprise.platform.push.controller

import com.proper.enterprise.platform.core.utils.ConfCenter
import com.proper.enterprise.platform.push.client.PusherApp
import com.proper.enterprise.platform.push.client.model.PushMessage
import com.proper.enterprise.platform.push.client.service.IPushApiServiceRequest
import com.proper.enterprise.platform.push.common.model.enums.PushMsgStatus
import com.proper.enterprise.platform.push.config.PushGlobalInfo
import com.proper.enterprise.platform.push.entity.PushDeviceEntity
import com.proper.enterprise.platform.push.repository.PushDeviceRepository
import com.proper.enterprise.platform.push.repository.PushMsgRepository
import com.proper.enterprise.platform.push.repository.PushUserRepository
import com.proper.enterprise.platform.push.test.PushAbstractTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql

@Sql([
    "/com/proper/enterprise/platform/push/push-users.sql",
    "/com/proper/enterprise/platform/push/push-devices.sql"
])
class AppServerRequestControllerTest extends PushAbstractTest {
    @Autowired
    PushUserRepository userRepo

    @Autowired
    PushDeviceRepository deviceRepo
    @Autowired
    PushMsgRepository msgRepository
    @Autowired
    PushGlobalInfo globalInfo

    PusherApp pusherApp
    PushMessage msg

    def vo

    @After
    void afterData() {
        delete(vo.getId())
    }

    @Before
    void init() {
        vo = initData()
        IPushApiServiceRequest pushApiRequestMock = new AppServerRequestMockService()
        pusherApp = new PusherApp("/appserver/request", VALID_APPKEY, "wanchina")
        pusherApp.setPushApiRequest(pushApiRequestMock)
        pusherApp.setAsync(false)

        msg = new PushMessage()
        msg.setTitle("titletest")
        msg.setContent("titlecontent")

        System.clearProperty("push_env")
        ConfCenter.reload()

        msgRepository.deleteAll()
    }

    @Test
    void doRequestMethodTest() {
        assert userRepo.count() == 2
        assert deviceRepo.findByAppkeyAndUserid(VALID_APPKEY, TEST_USERID1).size() == 3

        pusherApp.pushMessageToOneUser(msg, TEST_USERID1)
        assert msgRepository.count() == 3

        msgRepository.deleteAll()
        pusherApp.pushMessageToOneDevice(msg, PusherApp.DEVICE_TYPE_ANDROID, TEST_DEVICEID1)
        assert msgRepository.count() == 1

        msgRepository.deleteAll()
        pusherApp.pushMessageToAllUsers(msg)
        assert msgRepository.count() == 6

        msgRepository.deleteAll()
        pusherApp.pushMessageToAllDevices(msg)
        assert msgRepository.count() == 9

        msgRepository.deleteAll()
        pusherApp.pushMessageToAllDevices(msg, PusherApp.DEVICE_TYPE_ANDROID)
        assert msgRepository.count() == 6
        pusherApp.pushMessageToAllDevices(msg, PusherApp.DEVICE_TYPE_IOS)
        assert msgRepository.count() == 9

        msgRepository.deleteAll()
        pusherApp.setConnTimeout(1)
        assert pusherApp.getConnTimeout() == 1
        pusherApp.getPushApiRequest()
        pusherApp.setSecureKey("SecureKey")
        assert pusherApp.getSecureKey() == "SecureKey"
        pusherApp.setAppkey("Appkey")
        assert pusherApp.getAppkey() == "Appkey"
        assert pusherApp.isAsync() == false
        Runnable r = new Runnable() {
            @Override
            public void run() {

            }
        }
        pusherApp.startRunTask(r, true)
        pusherApp.setPushUrl("PushUrl")
        assert pusherApp.getPushUrl() == "PushUrl"
        pusherApp.setPushApiRequest(null)
        try {
            pusherApp.pushMessageToUsers(msg, [])
        } catch (Exception ex) {

        }
        try {
            pusherApp.pushMessageToDevices(msg, PusherApp.DEVICE_TYPE_ANDROID, [])
        } catch (Exception ex) {

        }
        try {
            pusherApp.pushMessageToAllUsers(msg)
        } catch (Exception ex) {

        }
        try {
            pusherApp.pushMessageToAllDevices(msg, PusherApp.DEVICE_TYPE_ANDROID)
        } catch (Exception ex) {

        }
        pusherApp = new PusherApp()


    }

    @Test
    void badgeNumberTest() {
        msg.setBadgeNumber(5)
        pusherApp.pushMessageToAllDevices(msg)
        msgRepository.findAll().each {
            assert it.getMcustomDatasMap()['_proper_badge'] == 5
        }
    }

    @Test
    void cmdMsgTest() {
        //cmd透传消息
        msg.setPushType(PushMessage.PUSHTYPE_CMD)
        pusherApp.pushMessageToAllDevices(msg)
        msgRepository.findAll().each {
            assert it.getMcustomDatasMap()['_proper_pushtype'] == 'cmd'
        }
    }

    @Test
    void reallySendTest() {
        //向第三方服务器推送消息,因为对应的token无效，所以发送应不会成功
        System.setProperty("push_env", "product")
        ConfCenter.reload()
        pusherApp.pushMessageToOneUser(msg, TEST_USERID1)
        msgRepository.findAll().each {
            assert it.getMstatus() == PushMsgStatus.UNSEND
        }

    }

    @Test
    void invalidAppkeyTest() {
        //不合法的appkey,推送的消息应该是0
        pusherApp.setAppkey("INVALID_APPKEY")
        pusherApp.pushMessageToAllDevices(msg)
        pusherApp.pushMessageToAllUsers(msg)
        pusherApp.pushMessageToOneUser(msg, TEST_USERID1)
        pusherApp.pushMessageToOneDevice(msg, PusherApp.DEVICE_TYPE_ANDROID, TEST_DEVICEID1)

        assert msgRepository.count() == 0
    }

    @Test
    void pushTokenEmptyTest() {
        //推送token为空的测试
        List<PushDeviceEntity> lstDevice = deviceRepo.findByAppkeyAndUserid(VALID_APPKEY, TEST_USERID1)
        List<String> tokens = []
        lstDevice.each {
            tokens.add(it.pushToken)
            it.setPushToken('')
        }
        deviceRepo.save(lstDevice)

        pusherApp.pushMessageToOneUser(msg, TEST_USERID1)

        msgRepository.findAll().each {
            assert it.getMstatus() == PushMsgStatus.UNSEND
        }

        tokens.eachWithIndex { it, i -> lstDevice[i].pushToken = it }
        deviceRepo.save(lstDevice)
    }


    @Test
    void emptyPushTest() {
        //当推送的设备或用户为空的测试
        pusherApp.pushMessageToDevices(msg, PusherApp.DEVICE_TYPE_ANDROID, [])
        pusherApp.pushMessageToUsers(msg, [])
        assert msgRepository.count() == 0
    }


    class AppServerRequestMockService implements IPushApiServiceRequest {
        @Override
        String requestServiceServer(String baseUrl, String methodName, Map<String, Object> params, int timeout)
            throws Exception {
            return JSONUtil.toJSON(pushRequest(baseUrl + "/" + methodName, params))
        }

        @Override
        String requestServiceServer(String baseUrl, Map<String, Object> params, int timeout) throws Exception {
            return JSONUtil.toJSON(pushRequest(baseUrl, params))
        }
    }
}
