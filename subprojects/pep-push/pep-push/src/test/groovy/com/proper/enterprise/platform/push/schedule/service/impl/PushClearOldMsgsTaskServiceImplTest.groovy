package com.proper.enterprise.platform.push.schedule.service.impl

import com.proper.enterprise.platform.push.client.model.PushMessage
import com.proper.enterprise.platform.push.config.PushGlobalInfo
import com.proper.enterprise.platform.push.repository.PushMsgRepository
import com.proper.enterprise.platform.push.common.schedule.service.PushClearOldMsgsTaskService
import com.proper.enterprise.platform.push.test.PushAbstractTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql

@Sql([
    "/com/proper/enterprise/platform/push/push-users.sql",
    "/com/proper/enterprise/platform/push/push-devices.sql",
    "/com/proper/enterprise/platform/push/push-msgs.sql"
])
class PushClearOldMsgsTaskServiceImplTest extends PushAbstractTest {
    @Autowired
    PushClearOldMsgsTaskService service

    @Autowired
    PushMsgRepository msgRepository

    @Autowired
    PushGlobalInfo globalInfo;
    def vo

    @Before
    void beforeInit() {
        vo = initData()
    }

    @After
    void afterData(){
        delete(vo.getId())
    }
    @Test
    void deleteOldMsgsTest() {
        assert msgRepository.count()>0
        service.deleteOldMsgs()
        assert msgRepository.count()==0
        //Exception 覆盖率
        Map<String, Map<String, Object>> config = globalInfo.getPushConfigs();
        Map<String, Map<String, Object>> temp = new HashMap<String, Map<String, Object>>();
        Set<String> appkeys = config.keySet();
        for (String appkey : appkeys) {
            temp.put(appkey,config.get(appkey));
        }
        for (String appkey : appkeys) {
            config.put(appkey,null);
        }
        try{
            service.deleteOldMsgs()
        }catch(Exception ex){
        }finally{
            for (String appkey : appkeys) {
                config.put(appkey,temp.get(appkey));
            }
        }
        PushMessage thePushmsg = new PushMessage("", "");
        thePushmsg = new PushMessage("", "",new HashMap<String, Object>());
        thePushmsg.addCustomData("CustomData","CustomData");
        thePushmsg.setCustoms(new HashMap<String, Object>())
        thePushmsg.setBadgeNumber(-1)
        thePushmsg.getBadgeNumber()
        thePushmsg.setBadgeNumber(1)
        thePushmsg.getBadgeNumber()
        thePushmsg.setPushType("")
        thePushmsg.getPushType()

    }
}
