package com.proper.enterprise.platform.push.schedule.service.impl

import com.proper.enterprise.platform.push.entity.PushDeviceEntity
import com.proper.enterprise.platform.push.entity.PushMsgEntity
import com.proper.enterprise.platform.push.repository.PushDeviceRepository
import com.proper.enterprise.platform.push.repository.PushMsgRepository
import com.proper.enterprise.platform.push.common.model.enums.PushMsgStatus
import com.proper.enterprise.platform.push.common.schedule.service.PushCheckUnsendMsgsService
import com.proper.enterprise.platform.push.test.PushAbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql

@Sql([
    "/com/proper/enterprise/platform/push/push-users.sql",
    "/com/proper/enterprise/platform/push/push-devices.sql",
    "/com/proper/enterprise/platform/push/push-msgs.sql"
])
class PushCheckUnsendMsgsServiceImplTest extends PushAbstractTest {
    @Autowired
    PushCheckUnsendMsgsService service

    @Autowired
    PushMsgRepository msgRepository

    @Autowired
    PushDeviceRepository deviceRepository



    @Test
    void saveCheckUnsendMsgsTest() {
        assert msgRepository.count()>0
        PushDeviceEntity device=deviceRepository.findByAppkeyAndDeviceid(VALID_APPKEY,TEST_DEVICEID1)
        PushMsgEntity msg=msgRepository.findByAppkeyAndDeviceAndUseridAndMstatus(VALID_APPKEY,device,TEST_USERID1,PushMsgStatus.UNSEND)[0]
        assert  msg !=null
        service.saveCheckUnsendMsgs()
        msg=msgRepository.findByAppkeyAndDeviceAndUseridAndMstatus(VALID_APPKEY,device,TEST_USERID1,PushMsgStatus.SENDED)[0]
        assert  msg!=null&&msg.getMstatus()==PushMsgStatus.SENDED && msg.getMsendedDate()!=null

    }
}
