package com.proper.enterprise.platform.push.schedule.service.impl

import com.proper.enterprise.platform.push.api.openapi.model.PushMessage
import com.proper.enterprise.platform.push.common.model.PushDevice
import com.proper.enterprise.platform.push.config.PushGlobalInfo
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

    @Autowired
    PushGlobalInfo globalInfo;


    @Test
    void saveCheckUnsendMsgsTest() {
        assert msgRepository.count()>0
        PushDeviceEntity device=deviceRepository.findByAppkeyAndDeviceid(VALID_APPKEY,TEST_DEVICEID1)
        PushMsgEntity msg=msgRepository.findByAppkeyAndDeviceAndUseridAndMstatus(VALID_APPKEY,device,TEST_USERID1,PushMsgStatus.UNSEND)[0]
        assert  msg !=null
        service.saveCheckUnsendMsgs()
        msg=msgRepository.findByAppkeyAndDeviceAndUseridAndMstatus(VALID_APPKEY,device,TEST_USERID1,PushMsgStatus.SENDED)[0]
        assert  msg!=null&&msg.getMstatus()==PushMsgStatus.SENDED && msg.getMsendedDate()!=null

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
            service.saveCheckUnsendMsgs()
        }catch(Exception ex){
        }finally{
            for (String appkey : appkeys) {
                config.put(appkey,temp.get(appkey));
            }
        }
        PushMessage thePushmsg = new PushMessage("", "");
        thePushmsg = new PushMessage("", "",new HashMap<String, Object>());
        thePushmsg.setId("id");
        thePushmsg.getId();
        thePushmsg.addCustomData("CustomData","CustomData");
        thePushmsg.addCustomDatas(new HashMap<String, Object>())
        device.getMstatus()
        msg.setMcustoms("Mcustoms")
        msg.setDevice((PushDevice)device)
        try{
            msg.setDevice((PushDevice)null)
        }catch(Exception ex){
        }
        msg.getAppkey()
        msg.getMsgid()
        msg.getUserid()
        msg.setMsendedDate(new Date())
        msg.getMsendedDate()
        msg.setMsendedDate(null)
        msg.getMsendedDate()
        msg.setMresponse("减肥，回荡着时刻发挥各种飞机号地块发的黑色礼服哈大多数法拉利圣诞节佛大受打击佛奥斯卡家具看了看的反对派的儿科的福建省地方尽量快点决定分开电饭锅发明的施工等方面亚尔股份依噶而又十分高雅而是驸马的说法妈个也减肥，回荡着时刻发挥各种飞机号地块发的黑色礼服哈大多数法拉利圣诞节佛大受打击佛奥斯卡家具看了看的反对派的儿科的福建省地方尽量快点决定分开电饭锅发明的施工等方面亚尔股份依噶而又十分高雅而是驸马的说法妈个也减肥，回荡着时刻发挥各种飞机号地块发的黑色礼服哈大多数法拉利圣诞节佛大受打击佛奥斯卡家具看了看的反对派的儿科的福建省地方尽量快点决定分开电饭锅发明的施工等方面亚尔股份依噶而又十分高雅而是驸马的说法妈个也减肥，回荡着时刻发挥各种飞机号地块发的黑色礼服哈大多数法拉利圣诞节佛大受打击佛奥斯卡家具看了看的反对派的儿科的福建省地方尽量快点决定分开电饭锅发明的施工等方面亚尔股份依噶而又十分高雅而是驸马的说法妈个也减肥，回荡着时刻发挥各种飞机号地块发的黑色礼服哈大多数法拉利圣诞节佛大受打击佛奥斯卡家具看了看的反对派的儿科的福建省地方尽量快点决定分开电饭锅发明的施工等方面亚尔股份依噶而又十分高雅而是驸马的说法妈个也减肥，回荡着时刻发挥各种飞机号地块发的黑色礼服哈大多数法拉利圣诞节佛大受打击佛奥斯卡家具看了看的反对派的儿科的福建省地方尽量快点决定分开电饭锅发明的施工等方面亚尔股份依噶而又十分高雅而是驸马的说法妈个也减肥，回荡着时刻发挥各种飞机号地块发的黑色礼服哈大多数法拉利圣诞节佛大受打击佛奥斯卡家具看了看的反对派的儿科的福建省地方尽量快点决定分开电饭锅发明的施工等方面亚尔股份依噶而又十分高雅而是驸马的说法妈个也减肥，回荡着时刻发挥各种飞机号地块发的黑色礼服哈大多数法拉利圣诞节佛大受打击佛奥斯卡家具看了看的反对派的儿科的福建省地方尽量快点决定分开电饭锅发明的施工等方面亚尔股份依噶而又十分高雅而是驸马的说法妈个也减肥，回荡着时刻发挥各种飞机号地块发的黑色礼服哈大多数法拉利圣诞节佛大受打击佛奥斯卡家具看了看的反对派的儿科的福建省地方尽量快点决定分开电饭锅发明的施工等方面亚尔股份依噶而又十分高雅而是驸马的说法妈个也减肥，回荡着时刻发挥各种飞机号地块发的黑色礼服哈大多数法拉利圣诞节佛大受打击佛奥斯卡家具看了看的反对派的儿科的福建省地方尽量快点决定分开电饭锅发明的施工等方面亚尔股份依噶而又十分高雅而")
        assert msg.getMresponse()=="减肥，回荡着时刻发挥各种飞机号地块发的黑色礼服哈大多数法拉利圣诞节佛大受打击佛奥斯卡家具看了看的反对派的儿科的福建省地方尽量快点决定分开电饭锅发明的施工等方面亚尔股份依噶而又十分高雅而是驸马的说法妈个也减肥，回荡着时刻发挥各种飞机号地块发的黑色礼服哈大多数法拉利圣诞节佛大受打击佛奥斯卡家具看了看的反对派的儿科的福建省地方尽量快点决定分开电饭锅发明的施工等方面亚尔股份依噶而又十分高雅而是驸马的说法妈个也减肥，回荡着时刻发挥各种飞机号地块发的黑色礼服哈大多数法拉利圣诞节佛大受打击佛奥斯卡家具看了看的反对派的儿科的福建省地方尽量快点决定分开电饭锅发明的施工等方面亚尔股份依噶而又十分高雅而是驸马的说法妈个也减肥，回荡着时刻发挥各种飞机号地块发的黑色礼服哈大多数法拉利圣诞节佛大受打击佛奥斯卡家具看了看的反对派的儿科的福建省地方尽量快点决定分开电饭锅发明的施工等方面亚尔股份依噶而又十分高雅而是驸马的说法妈个也减肥，回荡着时刻发挥各种飞机号地块发的黑色礼服哈大多数法拉利圣诞节佛大受打击佛奥斯卡家具看了看的反对派的儿科的福建省地方尽量快点决定分开电饭锅发明的施工等方面亚尔股份依噶而又十分高雅而是驸马的说法妈个也减肥，回荡着时刻发挥各种飞机号地块发的黑色礼服哈大多数法拉利圣诞节佛大受打击佛奥斯卡家具看了看的反对派的儿科的福建省地方尽量快点决定分开电饭锅发明的施工等方面亚尔股份依噶而又十分高雅而是驸马的说法妈个也减肥，回荡着时刻发挥各种飞机号地块发的黑色礼服哈大多数法拉利圣诞节佛大受打击佛奥斯卡家具看了看的反对派的儿科的福建省地方尽量快点决定分开电饭锅发明的施工等方面亚尔股份依噶而又十分高雅而是驸马的说法妈个也减肥，回荡着时刻发挥各种飞机号地块发的黑色礼服哈大多数法拉利圣诞节佛大受打击佛奥斯卡家具看了看的反对派的儿科的福建省地方尽量快点决定分开电饭锅发明的施工等方面亚尔股份依噶而又十分高雅而是驸马的说法妈个也减肥，回荡着时刻发挥各种飞机号地块发的黑色礼服哈大多数法拉利圣诞节佛大受打击佛奥斯卡家具看了看的反对派的儿科的福建省地方尽量快点决定分开电饭锅发明的施工等方面亚尔股份依噶而又十分高雅而是驸马的说法妈个也减肥，回荡着时刻发挥各种飞机号地块发的黑色礼服哈大多数法拉利圣诞节佛大受打击佛奥斯卡家具看了看的反对派的儿科的福建省地方尽量快点决定分开电饭锅发明的施工等方面亚尔股份依噶而又十分高雅"
        msg.setMresponse("1")
        assert msg.getMresponse()=="1"
    }
}
