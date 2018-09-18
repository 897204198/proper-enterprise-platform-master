package com.proper.enterprise.platform.notice.server.push.configurator.ios

import com.proper.enterprise.platform.api.auth.service.AccessTokenService
import com.proper.enterprise.platform.auth.common.vo.AccessTokenVO
import com.proper.enterprise.platform.core.utils.AntResourceUtil
import com.proper.enterprise.platform.file.vo.FileVO
import com.proper.enterprise.platform.notice.server.api.configurator.NoticeConfigurator
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

class IOSNoticeConfiguratorTest extends AbstractTest {

    @Autowired
    private NoticeConfigurator pushNoticeConfigurator

    @Autowired
    private AccessTokenService accessTokenService

    @Autowired
    private PushConfigMongoRepository pushConfigMongoRepository

    @Test
    public void iosConfPostTest() {
        String appKey = 'iosConfPostToken'
        post(appKey)
    }

    private FileVO post(String appKey) {
        def accessToken = new AccessTokenVO(appKey, 'for test using', appKey, 'GET:/test')
        accessTokenService.saveOrUpdate(accessToken)
        Map conf = new HashMap()
        conf.put("certPassword", IOSConstant.PASSWORD)
        conf.put("pushPackage", "1234")
        conf.put("certificateId", "111")
        Map request = new HashMap()
        request.put("pushChannel", PushChannelEnum.IOS.toString())
        try {
            pushNoticeConfigurator.post(appKey, conf, request)
        } catch (Exception e) {
            assert "ios cert is not find" == e.getMessage()
        }
        //上传测试证书
        Resource[] resources = AntResourceUtil.getResources(IOSConstant.CENT_PATH)
        String result = mockMvc.perform(
            MockMvcRequestBuilders
                .fileUpload("/file")
                .file(
                new MockMultipartFile("file", "icmp_dev_pro.txt", ",multipart/form-data", resources[0].inputStream)
            )
        ).andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn().getResponse().getContentAsString()
        FileVO fileVO = JSONUtil.parse(get("/file/" + result + "/meta", HttpStatus.OK).getResponse().getContentAsString(), FileVO.class)
        conf.put("certificateId", fileVO.getId())

        try {
            pushNoticeConfigurator.post(appKey, conf, request)
        } catch (Exception e) {
            assert "ios cert type must be p12" == e.getMessage()
        }

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
        assert fileP12VO.getFileName() == "icmp_dev_pro.p12"

        conf.put("certificateId", fileP12VO.getId())
        pushNoticeConfigurator.post(appKey, conf, request)
        PushConfDocument pushConf = pushConfigMongoRepository.findByAppKeyAndPushChannel(appKey, PushChannelEnum.IOS)
        assert pushConf.appKey == appKey
        return fileP12VO
    }

    @Test
    public void iosConfPutGetDelTest() {
        String appKey = 'iosConfPutGetDelToken'
        def accessToken = new AccessTokenVO(appKey, 'for test using', appKey, 'GET:/test')
        accessTokenService.saveOrUpdate(accessToken)
        Map request = new HashMap()
        request.put("pushChannel", PushChannelEnum.IOS.toString())
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
        assert fileP12VO.getFileName() == "icmp_dev_pro.p12"


        Map conf = new HashMap()
        conf.put("certPassword", "12345")
        conf.put("pushPackage", "6666")
        conf.put("certificateId", fileP12VO.getId())
        try {
            pushNoticeConfigurator.put(appKey, conf, request)
        } catch (Exception e) {
            assert e.getMessage().equals("Certificate and password do not match")
        }

        conf.put("certPassword", IOSConstant.PASSWORD)

        pushNoticeConfigurator.post(appKey, conf, request)
        PushConfDocument pushConf = pushConfigMongoRepository.findByAppKeyAndPushChannel(appKey, PushChannelEnum.IOS)
        assert pushConf.pushPackage == "6666"

        Map getConf = pushNoticeConfigurator.get(appKey, request)
        assert getConf.get("pushPackage") == "6666"

        pushNoticeConfigurator.delete(appKey, request)
        PushConfDocument delConf = pushConfigMongoRepository.findByAppKeyAndPushChannel(appKey, PushChannelEnum.IOS)
        assert null == delConf
    }

}
