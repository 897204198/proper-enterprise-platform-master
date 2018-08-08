package com.proper.enterprise.platform.push.service.impl

import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.push.client.PusherApp
import com.proper.enterprise.platform.push.client.model.PushMessage
import com.proper.enterprise.platform.push.common.model.enums.PushMode
import com.proper.enterprise.platform.push.config.PushGlobalInfo
import com.proper.enterprise.platform.push.entity.PushMsgEntity
import com.proper.enterprise.platform.push.repository.PushMsgRepository
import com.proper.enterprise.platform.push.service.PushMsgService
import com.proper.enterprise.platform.push.test.PushAbstractTest
import com.proper.enterprise.platform.push.vo.PushMsgVO
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.test.context.jdbc.Sql

@Sql([
    "/com/proper/enterprise/platform/push/push-users.sql",
    "/com/proper/enterprise/platform/push/push-devices.sql",
    "/com/proper/enterprise/platform/push/push-msgs.sql",
    "/com/proper/enterprise/platform/push/datadisc.sql"
])
class PushMsgServiceImplTest extends PushAbstractTest {
    @Autowired
    PushGlobalInfo pushGlobalInfo
    @Autowired
    PushMsgService pushMsgService
    @Autowired
    private PushMsgRepository pushMsgRepository;

    def vo

    @Before
    void beforeInit() {
        vo = initData()
    }

    @After
    void afterData() {
        delete(vo.getId())
    }

    @Test
    void getConvertAppkey() {

        PushMsgEntity entity = new PushMsgEntity()
        entity.setAppkey("test")
        Example example = Example.of(entity)
        Sort sort = new Sort(Sort.Direction.DESC, "lastModifyTime")
        PageRequest pageRequest = new PageRequest(0, 4, sort)
        List<PushMsgVO> list = pushMsgService.findByDateTypeAndAppkey(example, pageRequest).getData()
        assert list.size() == 4
        for (PushMsgVO vo : list) {
            assert "推送平台test".equals(vo.getAppkey())
        }
    }

    @Test
    void getConvertPushMode() {

        PushMsgEntity entity = new PushMsgEntity()
        entity.setPushMode(PushMode.huawei)
        Example example = Example.of(entity)
        Sort sort = new Sort(Sort.Direction.DESC, "lastModifyTime")
        PageRequest pageRequest = new PageRequest(0, 4, sort)
        List<PushMsgVO> list = pushMsgService.findByDateTypeAndAppkey(example, pageRequest).getData()
        assert list.size() == 2
        for (PushMsgVO vo : list) {
            assert "华为".equals(vo.getPushMode())
        }
    }

    @Test
    void savePushMessageToUsersTest() {
        PushMessage msg = new PushMessage()
        msg.setTitle("titletest")
        msg.setContent("titlecontent")
        List<String> list = new ArrayList()
        list.add(TEST_USERID1)
        pushMsgRepository.deleteAll()
        assert pushMsgRepository.count() == 0
        Map<String, String> requestParams = new LinkedHashMap<>()
        requestParams.put("appkey", VALID_APPKEY)
        requestParams.put("msg", JSONUtil.toJSON(msg).toString())
        requestParams.put("userids", JSONUtil.toJSON(list).toString())
        pushMsgService.savePushMessageToUsers(requestParams)
        assert pushMsgRepository.count() == 3
    }

    @Test
    void savePushMessageToAllUsersTest() {
        PushMessage msg = new PushMessage()
        msg.setTitle("titletest")
        msg.setContent("titlecontent")
        pushMsgRepository.deleteAll()
        assert pushMsgRepository.count() == 0
        Map<String, String> requestParams = new LinkedHashMap<>()
        requestParams.put("appkey", VALID_APPKEY)
        requestParams.put("msg", JSONUtil.toJSON(msg).toString())
        pushMsgService.savePushMessageToAllUsers(requestParams)
        assert pushMsgRepository.count() == 6
    }

    @Test
    void savePushMessageToDevicesTest() {
        PushMessage msg = new PushMessage()
        msg.setTitle("titletest")
        msg.setContent("titlecontent")
        List<String> list = new ArrayList()
        list.add(TEST_DEVICEID1)

        pushMsgRepository.deleteAll()
        assert pushMsgRepository.count() == 0

        Map<String, String> requestParams = new LinkedHashMap<>()
        requestParams.put("appkey", VALID_APPKEY)
        requestParams.put("msg", JSONUtil.toJSON(msg).toString())
        requestParams.put("deviceids", JSONUtil.toJSON(list).toString())
        requestParams.put("device_type", PusherApp.DEVICE_TYPE_ANDROID)
        pushMsgService.savePushMessageToDevices(requestParams)

        assert pushMsgRepository.count() == 1

        pushMsgRepository.deleteAll()
        requestParams.put("device_type", PusherApp.DEVICE_TYPE_IOS)
        pushMsgService.savePushMessageToDevices(requestParams)

        assert pushMsgRepository.count() == 0
    }

    @Test
    void savePushMessageToAllDevicesTest() {
        PushMessage msg = new PushMessage()
        msg.setTitle("titletest")
        msg.setContent("titlecontent")

        pushMsgRepository.deleteAll()
        assert pushMsgRepository.count() == 0

        Map<String, String> requestParams = new LinkedHashMap<>()
        requestParams.put("appkey", VALID_APPKEY)
        requestParams.put("msg", JSONUtil.toJSON(msg).toString())
        pushMsgService.savePushMessageToAllDevices(requestParams)

        assert pushMsgRepository.count() == 9
    }
}
