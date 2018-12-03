package com.proper.enterprise.platform.notice.server.push.sender.apns

import com.proper.enterprise.platform.api.auth.service.AccessTokenService
import com.proper.enterprise.platform.auth.common.vo.AccessTokenVO
import com.proper.enterprise.platform.core.exception.ErrMsgException
import com.proper.enterprise.platform.core.utils.AntResourceUtil
import com.proper.enterprise.platform.file.vo.FileVO
import com.proper.enterprise.platform.notice.server.api.configurator.NoticeConfigurator
import com.proper.enterprise.platform.notice.server.api.handler.NoticeSendHandler
import com.proper.enterprise.platform.notice.server.api.model.BusinessNoticeResult
import com.proper.enterprise.platform.notice.server.push.constant.IOSConstant
import com.proper.enterprise.platform.notice.server.push.dao.document.PushConfDocument
import com.proper.enterprise.platform.notice.server.push.dao.entity.PushNoticeMsgEntity
import com.proper.enterprise.platform.notice.server.push.dao.repository.PushConfigMongoRepository
import com.proper.enterprise.platform.notice.server.push.dao.repository.PushNoticeMsgJpaRepository
import com.proper.enterprise.platform.notice.server.push.enums.apns.IOSErrCodeEnum
import com.proper.enterprise.platform.notice.server.push.mock.MockPushNotice
import com.proper.enterprise.platform.notice.server.push.sender.AbstractPushSendSupport
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus
import com.proper.enterprise.platform.notice.server.sdk.enums.PushChannelEnum
import com.proper.enterprise.platform.notice.server.sdk.enums.PushDeviceTypeEnum
import com.proper.enterprise.platform.notice.server.sdk.enums.PushProfileEnum
import com.proper.enterprise.platform.test.AbstractJPATest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Ignore
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class ApnsNoticeSenderTest extends AbstractJPATest {

    @Autowired
    @Qualifier("iosNoticeSender")
    NoticeSendHandler iosNoticeSender

    @Autowired
    AccessTokenService accessTokenService

    @Autowired
    PushConfigMongoRepository pushConfigMongoRepository

    @Autowired
    PushNoticeMsgJpaRepository pushNoticeMsgJpaRepository

    @Autowired
    private NoticeConfigurator pushNoticeConfigurator

    @Test
    public void iosNoticeSendTest() {

        String appKey = 'iosConfSendToken'
        def accessToken = new AccessTokenVO(appKey, 'for test using', appKey, 'GET:/test')
        accessTokenService.saveOrUpdate(accessToken)

        //上传P12证书
        Resource[] resourcesP12 = AntResourceUtil.getResources(IOSConstant.CENT_PATH)
        String resultP12 = mockMvc.perform(
            MockMvcRequestBuilders
                .fileUpload("/file")
                .file(
                new MockMultipartFile("file", "icmp_dev_pro.p12", ",multipart/form-data", resourcesP12[0].inputStream)
            )
        ).andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn().getResponse().getContentAsString()
        FileVO fileP12VO = JSONUtil.parse(get("/file/" + resultP12 + "/meta", HttpStatus.OK).getResponse().getContentAsString(), FileVO.class)

        Map conf = new HashMap()
        conf.put("certPassword", IOSConstant.PASSWORD)
        conf.put("pushPackage", IOSConstant.TOPIC)
        conf.put("certificateId", fileP12VO.getId())

        Map request = new HashMap()
        request.put("pushChannel", PushChannelEnum.APNS.toString())
        pushNoticeConfigurator.post(appKey, conf, request)

        MockPushNotice mockPushNotice = new MockPushNotice()
        mockPushNotice.setAppKey(appKey)
        mockPushNotice.setTargetTo(IOSConstant.TARGET_TO)
        mockPushNotice.setTitle("555")
        mockPushNotice.setContent("66666qwe")
        mockPushNotice.setId("testtest")
        iosNoticeSender.send(mockPushNotice)
        waitExecutorDone()
        assert pushNoticeMsgJpaRepository.findPushNoticeMsgEntitiesByNoticeId("testtest").getContent() == "66666qwe"
        BusinessNoticeResult businessNoticeResult = iosNoticeSender.getStatus(mockPushNotice)
        assert NoticeStatus.SUCCESS == iosNoticeSender.getStatus(mockPushNotice).getNoticeStatus()

        //token无效严验证
        MockPushNotice mockPushNotice2 = new MockPushNotice()
        mockPushNotice2.setAppKey(appKey)
        mockPushNotice2.setTargetTo("553df1db87ab77af2ddc3410e7a68950bfb7165a65e73048a4376fed11a8c824")
        mockPushNotice2.setTitle("555")
        mockPushNotice2.setContent("66666qwe")
        mockPushNotice2.setId("testtest")
        //token未在包下
        BusinessNoticeResult notForTopic = iosNoticeSender.send(mockPushNotice2)
        assert IOSErrCodeEnum.DEVICE_TOKEN_NOT_FOR_TOPIC.getNoticeCode() == notForTopic.getCode()
        assert IOSErrCodeEnum.DEVICE_TOKEN_NOT_FOR_TOPIC.getCode() == notForTopic.getMessage()
        //token无效
        mockPushNotice2.setTargetTo("1231")
        BusinessNoticeResult badToken = iosNoticeSender.send(mockPushNotice2)
        assert IOSErrCodeEnum.BAD_DEVICE_TOKEN.getNoticeCode() == badToken.getCode()
        assert IOSErrCodeEnum.BAD_DEVICE_TOKEN.getCode() == badToken.getMessage()

        //todo Unregistered异常无法重现
    }

    @Test
    public void beforeSend() {
        String appKey = 'iosConfBeforeSendToken'
        def accessToken = new AccessTokenVO(appKey, 'for test using', appKey, 'GET:/test')
        accessTokenService.saveOrUpdate(accessToken)

        //上传P12证书
        Resource[] resourcesP12 = AntResourceUtil.getResources(IOSConstant.CENT_PATH)
        String resultP12 = mockMvc.perform(
            MockMvcRequestBuilders
                .fileUpload("/file")
                .file(
                new MockMultipartFile("file", "icmp_dev_pro.p12", ",multipart/form-data", resourcesP12[0].inputStream)
            )
        ).andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn().getResponse().getContentAsString()
        FileVO fileP12VO = JSONUtil.parse(get("/file/" + resultP12 + "/meta", HttpStatus.OK).getResponse().getContentAsString(), FileVO.class)

        PushConfDocument pushConfDocument = new PushConfDocument()
        pushConfDocument.setPushChannel(PushChannelEnum.APNS)
        pushConfDocument.setAppKey(appKey)
        pushConfDocument.setCertificateId(fileP12VO.getId())
        pushConfDocument.setCertPassword(IOSConstant.PASSWORD)

        PushConfDocument save = pushConfigMongoRepository.save(pushConfDocument)

        MockPushNotice mockPushNotice = new MockPushNotice()
        mockPushNotice.setAppKey(appKey)
        mockPushNotice.setTargetTo(IOSConstant.TARGET_TO)
        mockPushNotice.setTitle("555")
        mockPushNotice.setContent("66666qwe")
        try {
            iosNoticeSender.beforeSend(mockPushNotice)
        } catch (ErrMsgException e) {
            assert "apns push can't send without pushPackage".equals(e.getMessage())
        }
        save.setPushPackage(IOSConstant.TOPIC)
        PushConfDocument update = pushConfigMongoRepository.save(save)
        iosNoticeSender.beforeSend(mockPushNotice)
    }

    @Test
    @Ignore
    public void afterSend() {
        MockPushNotice mockPushNotice = new MockPushNotice()
        mockPushNotice.setTargetTo(IOSConstant.TARGET_TO)
        mockPushNotice.setTitle("555")
        mockPushNotice.setContent("66666qwe")
        mockPushNotice.setAppKey("appKeyAfterSend")
        mockPushNotice.setRetryCount(22)
        mockPushNotice.setStatus(NoticeStatus.SUCCESS)
        mockPushNotice.setTargetExtMsg(AbstractPushSendSupport.PUSH_CHANNEL_KEY, "IOS")
        iosNoticeSender.afterSend(mockPushNotice)
        waitExecutorDone()
        boolean flag = false
        for (PushNoticeMsgEntity pushNoticeMsg : pushNoticeMsgJpaRepository.findAll()) {
            if ("appKeyAfterSend" == pushNoticeMsg.getAppKey()
                && PushDeviceTypeEnum.IOS == pushNoticeMsg.getDeviceType()) {
                flag = true
            }
        }
        assert flag
        pushNoticeMsgJpaRepository.deleteAll()
    }

    @Ignore
    @Test
    public void iosNoticeSendDevTest() {
        // 测试机无法同时存在同一包名 development 和 production 的应用

        String appKey = 'iosConfSendDevToken'
        def accessToken = new AccessTokenVO(appKey, 'for test using', appKey, 'GET:/test')
        accessTokenService.saveOrUpdate(accessToken)

        //上传P12证书
        Resource[] resourcesP12 = AntResourceUtil.getResources(IOSConstant.CENT_PATH_DEV)
        String resultP12 = mockMvc.perform(
            MockMvcRequestBuilders
                .fileUpload("/file")
                .file(
                new MockMultipartFile("file", "icmp.dev-dev.p12", ",multipart/form-data", resourcesP12[0].inputStream)
            )
        ).andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn().getResponse().getContentAsString()
        FileVO fileP12VO = JSONUtil.parse(get("/file/" + resultP12 + "/meta", HttpStatus.OK).getResponse().getContentAsString(), FileVO.class)

        Map conf = new HashMap()
        conf.put("certPassword", IOSConstant.PASSWORD_DEV)
        conf.put("pushPackage", IOSConstant.TOPIC)
        conf.put("certificateId", fileP12VO.getId())
        conf.put("pushProfile", PushProfileEnum.DEV)

        Map request = new HashMap()
        request.put("pushChannel", PushChannelEnum.APNS.toString())
        pushNoticeConfigurator.post(appKey, conf, request)

        MockPushNotice mockPushNotice = new MockPushNotice()
        mockPushNotice.setAppKey(appKey)
        mockPushNotice.setTargetTo(IOSConstant.TARGET_TO_DEV)
        mockPushNotice.setTitle("555")
        mockPushNotice.setContent("66666qwe")
        mockPushNotice.setId("testtest")
        iosNoticeSender.send(mockPushNotice)
        waitExecutorDone()
        assert pushNoticeMsgJpaRepository.findPushNoticeMsgEntitiesByNoticeId("testtest").getContent() == "66666qwe"
        BusinessNoticeResult businessNoticeResult = iosNoticeSender.getStatus(mockPushNotice)
        assert NoticeStatus.SUCCESS == iosNoticeSender.getStatus(mockPushNotice).getNoticeStatus()
    }
}
