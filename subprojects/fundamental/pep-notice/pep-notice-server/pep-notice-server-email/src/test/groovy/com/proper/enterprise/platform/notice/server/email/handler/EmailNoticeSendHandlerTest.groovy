package com.proper.enterprise.platform.notice.server.email.handler

import com.proper.enterprise.platform.core.i18n.I18NUtil
import com.proper.enterprise.platform.core.utils.AntResourceUtil
import com.proper.enterprise.platform.file.vo.FileVO
import com.proper.enterprise.platform.notice.server.api.configurator.NoticeConfigurator
import com.proper.enterprise.platform.notice.server.api.handler.NoticeSendHandler
import com.proper.enterprise.platform.notice.server.api.model.Notice
import com.proper.enterprise.platform.notice.server.email.entity.MockNotice
import com.proper.enterprise.platform.test.AbstractJPATest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class EmailNoticeSendHandlerTest extends AbstractJPATest {

    @Autowired
    private NoticeSendHandler emailNoticeSender

    @Autowired
    private NoticeConfigurator emailNoticeConfigurator

    @Test
    void testSendEmail() {
        //上传附件
        Resource[] resources = AntResourceUtil.getResources("classpath*:com/proper/enterprise/platform/notice/server/email/test.txt")
        String result = mockMvc.perform(
            MockMvcRequestBuilders
                .fileUpload("/file")
                .file(
                new MockMultipartFile("file", "test.txt", ",multipart/form-data", resources[0].inputStream)
            )
        ).andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn().getResponse().getContentAsString()
        FileVO fileVO = JSONUtil.parse(get("/file/" + result + "/meta", HttpStatus.OK).getResponse().getContentAsString(), FileVO.class)

        Resource[] resources2 = AntResourceUtil.getResources("classpath*:com/proper/enterprise/platform/notice/server/email/测试.txt")
        String result2 = mockMvc.perform(
            MockMvcRequestBuilders
                .fileUpload("/file")
                .file(
                new MockMultipartFile("file", "测试.txt", ",multipart/form-data", resources2[0].inputStream)
            )
        ).andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn().getResponse().getContentAsString()
        FileVO fileVO2 = JSONUtil.parse(get("/file/" + result2 + "/meta", HttpStatus.OK).getResponse().getContentAsString(), FileVO.class)

        try {
            def config = [:]
            config.put('mailServerHost', 'smtp.exmail.qq.com')
            config.put('mailServerPort', 465)
            config.put('mailServerUsername', 'test@test.cn')
            config.put('mailServerPassword', 'abcd')
            config.put('mailServerUseSSL', true)
            config.put('mailServerDefaultFrom', 'test@test.cn')

            emailNoticeConfigurator.post('pep', config,null)

            config.put('mailServerHost', 'smtp.exmail.qq.com')
            config.put('mailServerPort', 465)
            config.put('mailServerUsername', 'test2@test.cn')
            config.put('mailServerPassword', 'abdd')
            config.put('mailServerUseSSL', true)
            config.put('mailServerDefaultFrom', '测试邮箱<test2@test.cn>')

            emailNoticeConfigurator.post('icmp', config,null)

            //测试邮件全参数配置
            Notice noticeOperation2 = new MockNotice()
            noticeOperation2.setAppKey('icmp')
            noticeOperation2.setTargetTo('收件人<test@test.cn>,收件人<test2@test.cn>')
            noticeOperation2.setTargetExtMsg('cc', '抄送<test2@test.cn>')
            noticeOperation2.setTargetExtMsg('bcc', '密送<test2@test.cn>')
            noticeOperation2.setTargetExtMsg('replyTo', '回复到<test@test.cn>')
            def fileIds = [fileVO2.getId()]
            noticeOperation2.setTargetExtMsg('attachmentIds', fileIds)
            noticeOperation2.setNoticeExtMsg('from', '测试邮箱<test2@test.cn>')
            //设置发送时间
            noticeOperation2.setNoticeExtMsg('sentDate', '2017-12-29T10:23:23.998Z')
            noticeOperation2.setTitle('测试邮件')
            noticeOperation2.setContent('测试邮件, 请勿回复')
            emailNoticeSender.send(noticeOperation2)

            // 测试正常发送
            Notice noticeOperation1 = new MockNotice()
            noticeOperation1.setAppKey('pep')
            noticeOperation1.setTargetTo('收件人<test2@test.cn>')
            def attachmentIds = [fileVO.getId(), fileVO2.getId()]
            noticeOperation1.setTargetExtMsg('attachmentIds', attachmentIds)
            noticeOperation1.setTitle('测试邮件')
            noticeOperation1.setContent('测试邮件, 请勿回复')
            emailNoticeSender.send(noticeOperation1)

            Notice noticeOperation3 = new MockNotice()
            noticeOperation3.setAppKey('pep')
            emailNoticeSender.send(noticeOperation3)
        } catch (Exception e) {
            assert e.getMessage().contains(I18NUtil.getMessage("pep.email.notice.send.error"))
        }
    }
}
