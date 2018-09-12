package com.proper.enterprise.platform.notice.server.push.client.ios

import com.proper.enterprise.platform.core.exception.ErrMsgException
import com.proper.enterprise.platform.core.utils.AntResourceUtil
import com.proper.enterprise.platform.file.vo.FileVO
import com.proper.enterprise.platform.notice.server.push.constant.IOSConstant
import com.proper.enterprise.platform.notice.server.push.dao.document.PushConfDocument
import com.proper.enterprise.platform.notice.server.push.dao.repository.PushConfigMongoRepository
import com.proper.enterprise.platform.notice.server.push.enums.PushChannelEnum
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class IOSClientManagerTest extends AbstractTest {

    @Autowired
    IOSNoticeClientManagerApi iosNoticeClientApi

    @Autowired
    PushConfigMongoRepository pushConfigMongoRepository

    @Test
    public void clientTest() {
        try {
            iosNoticeClientApi.get()
        } catch (ErrMsgException e) {
            assert "appKey can't be empty" == e.getMessage()
        }
        String appKey = "testIOS"
        try {
            iosNoticeClientApi.get(appKey)
        } catch (ErrMsgException e) {
            assert "can't find conf by appKey:" + appKey == e.getMessage()
        }

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

        pushConfDocument.setAppKey(appKey)
        pushConfDocument.setCertificateId(fileP12VO.getId())
        pushConfDocument.setP12Password(IOSConstant.PASSWORD)
        pushConfDocument.setPushChannel(PushChannelEnum.IOS)
        pushConfigMongoRepository.save(pushConfDocument)

        assert null != iosNoticeClientApi.get(appKey)
    }
}
