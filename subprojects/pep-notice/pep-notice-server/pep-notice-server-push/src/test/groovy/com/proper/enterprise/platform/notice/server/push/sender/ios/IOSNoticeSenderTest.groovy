package com.proper.enterprise.platform.notice.server.push.sender.ios

import com.proper.enterprise.platform.api.auth.service.AccessTokenService
import com.proper.enterprise.platform.auth.common.vo.AccessTokenVO
import com.proper.enterprise.platform.core.utils.AntResourceUtil
import com.proper.enterprise.platform.file.vo.FileVO
import com.proper.enterprise.platform.notice.server.api.handler.NoticeSendHandler
import com.proper.enterprise.platform.notice.server.push.constant.IOSConstant
import com.proper.enterprise.platform.notice.server.push.mock.MockPushNotice
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class IOSNoticeSenderTest extends AbstractTest {

    @Autowired
    @Qualifier("iosNoticeSender")
    NoticeSendHandler iosNoticeSender

    @Autowired
    AccessTokenService accessTokenService

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
        conf.put("p12Password", IOSConstant.PASSWORD)
        conf.put("pushPackage", IOSConstant.TOPIC)
        conf.put("certificateId", fileP12VO.getId())
        post('/notice/server/config/' + NoticeType.PUSH + "?accessToken=" +
            appKey + "&pushChannel=IOS", JSONUtil.toJSON(conf), HttpStatus.CREATED)

        MockPushNotice mockPushNotice = new MockPushNotice()
        mockPushNotice.setAppKey(appKey)
        mockPushNotice.setTargetTo(IOSConstant.TARGET_TO)
        mockPushNotice.setTitle("555")
        mockPushNotice.setContent("66666qwe")
        iosNoticeSender.send(mockPushNotice)
    }
}
