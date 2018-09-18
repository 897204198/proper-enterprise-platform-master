package com.proper.enterprise.platform.notice.service

import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.core.utils.StringUtil
import com.proper.enterprise.platform.core.utils.http.HttpClient
import com.proper.enterprise.platform.notice.entity.NoticeSetDocument
import com.proper.enterprise.platform.notice.repository.NoticeMsgRepository
import com.proper.enterprise.platform.notice.repository.NoticeSetRepository
import com.proper.enterprise.platform.sys.datadic.DataDic
import com.proper.enterprise.platform.sys.datadic.repository.DataDicRepository
import com.proper.enterprise.platform.sys.datadic.util.DataDicUtil
import com.proper.enterprise.platform.template.document.TemplateDocument
import com.proper.enterprise.platform.template.repository.TemplateRepository
import com.proper.enterprise.platform.template.vo.TemplateDetailVO
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.context.jdbc.Sql

@Ignore
@Sql
class NoticeClientToServerTest extends AbstractTest {

    @Autowired
    UserService userService

    @Autowired
    NoticeSender noticeSender

    @Autowired
    NoticeMsgRepository noticeMsgRepository

    @Autowired
    NoticeSetRepository noticeSetRepository

    @Autowired
    TemplateRepository templateRepository

    @Autowired
    private DataDicRepository repository;

    @Test
    void configEmail() {
        String noticeServerUrl = null;
        DataDic dataDic = DataDicUtil.get("NOTICE_SERVER", "URL");
        if (dataDic != null) {
            noticeServerUrl = dataDic.getName();
        }
        String noticeServerToken = null;
        dataDic = DataDicUtil.get("NOTICE_SERVER", "TOKEN");
        if (dataDic != null) {
            noticeServerToken = dataDic.getName();
        }
        Map<String, String> headers = new HashMap<>(1)
        headers.put("X-PEP-TOKEN", noticeServerToken)
        ResponseEntity<byte[]> response = HttpClient.post(noticeServerUrl
            + "/notice/server/config/EMAIL" + headers
            + noticeServerToken, MediaType.APPLICATION_JSON, '{"mailServerHost":"smtp.exmail.qq.com","mailServerPort":465,"mailServerUsername":"Wf@propersoft.cn","mailServerPassword":"9x5qDmxsyrzMra5W","mailServerUseSSL":true,"mailServerDefaultFrom":"Wf@propersoft.cn"}');
        println StringUtil.toEncodedString(response.getBody())
    }

    @Test
    void configSMS() {
        String noticeServerUrl = null;
        DataDic dataDic = DataDicUtil.get("NOTICE_SERVER", "URL");
        if (dataDic != null) {
            noticeServerUrl = dataDic.getName();
        }
        String noticeServerToken = null;
        dataDic = DataDicUtil.get("NOTICE_SERVER", "TOKEN");
        if (dataDic != null) {
            noticeServerToken = dataDic.getName();
        }
        Map<String, String> headers = new HashMap<>(1)
        headers.put("X-PEP-TOKEN", noticeServerToken)
        ResponseEntity<byte[]> response = HttpClient.post(noticeServerUrl
            + "/notice/server/config/SMS" + headers
            + noticeServerToken, MediaType.APPLICATION_JSON, '{"smsUrl":"http://118.145.22.173:9887/smsservice/SendSMS","smsSend":"465","smsCharset":"GBK"}');
        println StringUtil.toEncodedString(response.getBody())
    }

    @Test
    void sendEmailAndSms() {
        Map<String, Object> custom = new HashMap<>()
        custom.put("url", "1")
        Map<String, Object> templateParams = new HashMap<>()
        templateParams.put("pageurl", "2")
        templateParams.put("processDefinitionName", "請假申請")
        noticeSender.sendNotice("test1", "EndBpmCode", templateParams, custom)
    }

    @Before
    void init() {

        NoticeSetDocument noticeSetDocument1 = new NoticeSetDocument()
        noticeSetDocument1.setUserId("test1")
        noticeSetDocument1.setCatalog("BPM")
        noticeSetDocument1.setPush(true)
        noticeSetDocument1.setSms(true)
        noticeSetDocument1.setEmail(true)
        noticeSetRepository.save(noticeSetDocument1)

        NoticeSetDocument noticeSetDocument2 = new NoticeSetDocument()
        noticeSetDocument2.setUserId("test2")
        noticeSetDocument2.setCatalog("BPM")
        noticeSetDocument2.setPush(true)
        noticeSetDocument2.setSms(true)
        noticeSetDocument2.setEmail(true)
        noticeSetRepository.save(noticeSetDocument2)

        TemplateDocument templateDocument = new TemplateDocument()
        templateDocument.code = "EndBpmCode"
        templateDocument.name = "EndBpmName"
        templateDocument.catalog = "BPM"
        templateDocument.muti = true
        List<TemplateDetailVO> details = new ArrayList<>()
        TemplateDetailVO detail1 = new TemplateDetailVO()
        detail1.title = "流程完结"
        detail1.template = "您的【{processDefinitionName}】已完成，详情请在【发起历史】中查看。"
        detail1.type = "EMAIL"
        details.add(detail1)
        TemplateDetailVO detail2 = new TemplateDetailVO()
        detail2.title = "您有一条已办消息"
        detail2.template = "您的{processDefinitionName}已完成"
        detail2.type = "PUSH"
        details.add(detail2)
        TemplateDetailVO detail3 = new TemplateDetailVO()
        detail3.title = "短信"
        detail3.template = "您的請假申請已經完成"
        detail3.type = "SMS"
        details.add(detail3)
        templateDocument.details = details
        templateRepository.save(templateDocument)
    }

    @After
    void destroy() {
        noticeSetRepository.deleteAll()
        templateRepository.deleteAll()
    }

}
