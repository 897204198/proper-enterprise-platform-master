package com.proper.enterprise.platform.push.vendor

import com.proper.enterprise.platform.push.entity.PushMsgEntity
import com.proper.enterprise.platform.push.test.PushAbstractTest
import org.junit.Test

class BasePushAppTest extends PushAbstractTest {

    @Test
    void getBadgeNumberTest() {
        BasePushApp basePushApp = new BasePushApp();
        Map<String, Object> map = new HashMap<String, Object>()
        map.put("_proper_badge",Integer.valueOf(-1))
        PushMsgEntity msg = new PushMsgEntity()
        msg.setMcustomDatasMap(map)
        assert basePushApp.getBadgeNumber(msg) == 0

        map.put("_proper_badge","a")
        msg.setMcustomDatasMap(map)
        try{
            basePushApp.getBadgeNumber(msg)
        }catch(Exception ex){
        }

        basePushApp.setPushService(null)
        assert basePushApp.getPushService() == null
    }
}
