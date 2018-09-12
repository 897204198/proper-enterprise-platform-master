package com.proper.enterprise.platform.push.schedule.service.impl

import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.push.api.openapi.service.PushChannelService
import com.proper.enterprise.platform.push.entity.PushChannelEntity
import com.proper.enterprise.platform.push.vo.PushChannelVO
import com.proper.enterprise.platform.sys.i18n.I18NUtil
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class PushChannelTest extends AbstractTest {

    @Autowired
    private PushChannelService pushChannelService;

    def url = "/push/channels"

    @Test
    void testConvertVoToDocument() {
        def pushChannelVo = initVo()
        def document = PushChannelVO.convertVoToEntity(pushChannelVo)
        assert document != null
        try {
            PushChannelVO.convertVoToEntity(null)
        } catch (Exception e) {
            assert I18NUtil.getMessage("pep.push.formatting.error").equals(e.message)
        }

    }

    @Test
    void testConvertDocumentToVo() {
        def pushChannelDocument = initDocument()
        def pushChannelVo = PushChannelVO.convertEntityToVo(pushChannelDocument)
        assert pushChannelVo != null
        try {
            PushChannelVO.convertEntityToVo(null)
        } catch (Exception e) {
            assert I18NUtil.getMessage("pep.push.formatting.error").equals(e.message)
        }
    }

    @Test
    void testChannel() {
        def vo = initVo()
        vo = addChannel(vo)
        assert vo != null

        def id = vo.getId()
        vo = initVo()
        vo.setId(id)
        vo.getAndroid().getHuawei().setTheAppId("app_id222")
        vo.setMsgSaveDays(2)
        vo = updateChannel(vo)
        assert vo != null

        assert vo.getAndroid().getHuawei().getTheAppId().equals("app_id222")
        assert vo.getMsgSaveDays() == 2

        assert getChannel().count == 1
        assert getChannel().data.findAll().get(0).enable == true
        assert getChannel().data.findAll().get(0).color == "red"
        deleteChannel(vo.getId())
        assert getChannel().count == 0

        assert HttpStatus.INTERNAL_SERVER_ERROR.value() == put(url, JSONUtil.toJSON(vo), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getStatus()
        addErrorChannel()

    }

    PushChannelVO addChannel(PushChannelVO pushChannelVo) {
        def resultContent = post(url, JSONUtil.toJSON(pushChannelVo), HttpStatus.CREATED).getResponse().getContentAsString()
        return JSONUtil.parse(resultContent, PushChannelVO.class)
    }


    void addErrorChannel() {
        def map = new HashMap<>()
        map.put("name", "mobile")
        map.put("desc", "aaa")
        map.put("msgSaveDays", "1")
        map.put("maxSendCount", "1")
        map.put("secretKey", "1")
        map.put("android", "asdsda")
        map.put("ios", "asdsda")
        map.put("diplomaId", "asdsda")
        assert HttpStatus.BAD_REQUEST.value() == post(url, JSONUtil.toJSON(map), HttpStatus.BAD_REQUEST).getResponse().getStatus()

        def huaweiMap = new HashMap<>()
        huaweiMap.put("theAppId", null)
        huaweiMap.put("theAppSecret", "aa")
        huaweiMap.put("theAppPackage", "aa")

        def xiaomiMap = new HashMap<>()
        xiaomiMap.put("theAppSecret", "aa")
        xiaomiMap.put("theAppPackage", "aa")

        def androidMap = new HashMap<>()
        androidMap.put("huawei", huaweiMap)
        androidMap.put("xiaomi", xiaomiMap)

        map.put("android", androidMap)
        map.remove("ios")
        assert HttpStatus.INTERNAL_SERVER_ERROR.value() == post(url, JSONUtil.toJSON(map), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getStatus()

        huaweiMap.put("theAppId", "aa")
        huaweiMap.put("theAppSecret", null)
        assert HttpStatus.INTERNAL_SERVER_ERROR.value() == post(url, JSONUtil.toJSON(map), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getStatus()

        huaweiMap.put("theAppSecret", "aa")
        xiaomiMap.put("theAppSecret", null)
        assert HttpStatus.INTERNAL_SERVER_ERROR.value() == post(url, JSONUtil.toJSON(map), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getStatus()

        xiaomiMap.put("theAppSecret", "aa")
        xiaomiMap.put("theAppPackage", null)
        assert HttpStatus.INTERNAL_SERVER_ERROR.value() == post(url, JSONUtil.toJSON(map), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getStatus()

        xiaomiMap.put("theAppPackage", "aa")
        androidMap.put("huawei", null)
        assert HttpStatus.INTERNAL_SERVER_ERROR.value() == post(url, JSONUtil.toJSON(map), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getStatus()

        androidMap.put("huawei", huaweiMap)
        androidMap.put("xiaomi", null)
        assert HttpStatus.INTERNAL_SERVER_ERROR.value() == post(url, JSONUtil.toJSON(map), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getStatus()

        androidMap.put("xiaomi", xiaomiMap)
        map.remove("diplomaId")
        assert HttpStatus.CREATED.value() == post(url, JSONUtil.toJSON(map), HttpStatus.CREATED).getResponse().getStatus()

        def iosMap = new HashMap<>()
        map.put("ios", iosMap)
        map.put("diplomaId", "aaa")
        iosMap.put("keystorePassword", null)
        iosMap.put("topic", "aa")
        assert HttpStatus.INTERNAL_SERVER_ERROR.value() == post(url, JSONUtil.toJSON(map), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getStatus()

        iosMap.put("keystorePassword", "aa")
        iosMap.put("topic", null)
        assert HttpStatus.INTERNAL_SERVER_ERROR.value() == post(url, JSONUtil.toJSON(map), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getStatus()

        iosMap.put("topic", "aa")
        assert HttpStatus.INTERNAL_SERVER_ERROR.value() == post(url, JSONUtil.toJSON(map), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getStatus()

        map.put("id", "bbb")
        assert HttpStatus.CREATED.value() == post(url, JSONUtil.toJSON(map), HttpStatus.CREATED).getResponse().getStatus()

    }

    PushChannelVO updateChannel(PushChannelVO pushChannelVo) {
        def resultContent = put(url, JSONUtil.toJSON(pushChannelVo), HttpStatus.OK).getResponse().getContentAsString()

        return JSONUtil.parse(resultContent, PushChannelVO.class)
    }

    DataTrunk<PushChannelVO> getChannel() {
        def resultContent = get(url, HttpStatus.OK).getResponse().getContentAsString()
        return (DataTrunk<PushChannelVO>) JSONUtil.parse(resultContent, Object.class)
    }

    void deleteChannel(String ids) {
        delete(url + "?ids=" + ids, HttpStatus.NO_CONTENT)
    }

    PushChannelVO initVo() {
        PushChannelVO pushChannelVo = new PushChannelVO()
        pushChannelVo.setId("aaa")
        pushChannelVo.setChannelDesc("医院")
        pushChannelVo.setChannelName("MobileOA")
        pushChannelVo.setDiplomaId("aaa")
        pushChannelVo.setMaxSendCount(1)
        pushChannelVo.setMsgSaveDays(1)
        pushChannelVo.setSecretKey("aaaaa")
        pushChannelVo.setAndroid(getAndroid())
        pushChannelVo.setIos(getIOS())
        pushChannelVo.setEnable(true)
        pushChannelVo.setColor("red")
        return pushChannelVo
    }

    PushChannelVO.Android getAndroid() {
        PushChannelVO.Android android = new PushChannelVO.Android()
        android.setHuawei(new PushChannelVO.Android.HuaweiBean("app_id", "app_secret", "the_app_package"))
        android.setXiaomi(new PushChannelVO.Android.XiaomiBean("app_secret", "app_package"))
        return android
    }

    PushChannelVO.IOS getIOS() {
        PushChannelVO.IOS ios = new PushChannelVO.IOS()
        ios.setEnvProduct(true)
        ios.setKeystorePassword("1234")
        ios.setTopic("com.xx.xx")
        return ios
    }

    PushChannelEntity initDocument() {
        PushChannelEntity pushChannelDocument = new PushChannelEntity()
        pushChannelDocument.setId("aaa")
        pushChannelDocument.setChannelDesc("医院")
        pushChannelDocument.setChannelName("MobileOA")
        pushChannelDocument.setDiplomaId("aaa")
        pushChannelDocument.setMaxSendCount(1)
        pushChannelDocument.setMsgSaveDays(1)
        pushChannelDocument.setSecretKey("aaaaa")
        pushChannelDocument.setAndroid("{ \"huawei\": { \"theAppId\": \"X\", \"theAppSecret\": \"XX\", \"theAppPackage\": \"XX\"}, \"xiaomi\": { \"theAppSecret\": \"XXX\", \"theAppPackage\": \"sss.sss.sss.sss\" } }")
        pushChannelDocument.setIos("{ \"envProduct\": true, \"keystorePassword\": \"1234\", \"topic\": \"sss.sss.sss.sss\" }")
        return pushChannelDocument
    }

    @Test
    void testFindChannel() {
        PushChannelVO pushChannelVo = new PushChannelVO()
        pushChannelVo.setId("aaa")
        pushChannelVo.setChannelDesc("医院")
        pushChannelVo.setChannelName("MobileOA")
        pushChannelVo.setDiplomaId("aaa")
        pushChannelVo.setMaxSendCount(1)
        pushChannelVo.setMsgSaveDays(1)
        pushChannelVo.setSecretKey("aaaaa")
        pushChannelVo.setAndroid(getAndroid())
        pushChannelVo.setIos(getIOS())
        pushChannelVo.setEnable(true)
        pushChannelVo.setColor("red")

        PushChannelVO pushChannelVo2 = new PushChannelVO()
        pushChannelVo2.setId("bbb")
        pushChannelVo2.setChannelDesc("银行")
        pushChannelVo2.setChannelName("MobileOB")
        pushChannelVo2.setDiplomaId("bbb")
        pushChannelVo2.setMaxSendCount(1)
        pushChannelVo2.setMsgSaveDays(1)
        pushChannelVo2.setSecretKey("bbbbb")
        pushChannelVo2.setAndroid(getAndroid())
        pushChannelVo2.setIos(getIOS())
        pushChannelVo2.setEnable(false)
        pushChannelVo2.setColor("blue")
        addChannel(pushChannelVo)
        addChannel(pushChannelVo2)

        DataTrunk<PushChannelVO> pushList = pushChannelService.findByEnable()
        assert pushList.getData().size() == 1

        DataTrunk<PushChannelVO> pushs = pushChannelService.findAll()
        assert pushs.getData().size() == 2
    }


}
