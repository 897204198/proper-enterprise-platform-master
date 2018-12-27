package com.proper.enterprise.platform.notice.controller

import com.proper.enterprise.platform.notice.entity.PushDeviceEntity
import com.proper.enterprise.platform.notice.repository.PushDeviceRepository
import com.proper.enterprise.platform.notice.service.PushDeviceService
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.After
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class PushDeviceControllerTest extends AbstractJPATest {

    @Autowired
    PushDeviceRepository deviceRepo

    @Autowired
    PushDeviceService pushDeviceService

    @After
    void afterData() {
        deviceRepo.deleteAll();
    }

    void saveAndCheck(String appKey, String pushToken, String userId, String deviceId) {
        Map<String, Object> param = new HashMap<>()
        //注册设备
        param.put("appkey", appKey)
        param.put("unbind_other_device", "true")
        param.put("push_token", pushToken)
        param.put("userid", userId)
        param.put("deviceid", deviceId)
        post("/push/device?appkey=" + appKey + "&unbind_other_device=true&push_token=" + pushToken + "&userid=" + userId + "&deviceid=" + deviceId, "", HttpStatus.OK)
    }

    @Test
    void saveAndList() {
        this.saveAndCheck("appkey1", "push_token1", "userid1", "deviceid1")
        PushDeviceEntity p = deviceRepo.findByUserId("userid1")
        assert p.userId == "userid1"

        this.saveAndCheck("appkey1", "push_token2", "userid2", "deviceid1")
        this.saveAndCheck("appkey2", "push_token4", "userid4", "deviceid3")
        List<String> list = pushDeviceService.findUserIdsByAppKey("appkey1")
        assert list.size() == 2
    }
}
