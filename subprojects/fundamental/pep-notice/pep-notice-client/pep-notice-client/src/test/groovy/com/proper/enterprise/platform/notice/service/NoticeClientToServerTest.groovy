package com.proper.enterprise.platform.notice.service

import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.core.utils.StringUtil
import com.proper.enterprise.platform.core.utils.http.HttpClient
import com.proper.enterprise.platform.notice.document.NoticeSetDocument
import com.proper.enterprise.platform.notice.entity.PushDeviceEntity
import com.proper.enterprise.platform.notice.enums.PushDeviceType
import com.proper.enterprise.platform.notice.enums.PushMode
import com.proper.enterprise.platform.notice.repository.NoticeMsgRepository
import com.proper.enterprise.platform.notice.repository.NoticeSetRepository
import com.proper.enterprise.platform.notice.repository.PushDeviceRepository
import com.proper.enterprise.platform.sys.datadic.DataDic
import com.proper.enterprise.platform.sys.datadic.repository.DataDicRepository
import com.proper.enterprise.platform.sys.datadic.util.DataDicUtil
import com.proper.enterprise.platform.template.entity.TemplateEntity
import com.proper.enterprise.platform.template.repository.TemplateRepository
import com.proper.enterprise.platform.template.vo.TemplateDetailVO
import com.proper.enterprise.platform.test.AbstractJPATest
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
class NoticeClientToServerTest extends AbstractJPATest {

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
    DataDicRepository repository

    @Autowired
    PushDeviceRepository deviceRepository

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
            + "/rest/notice/server/config/EMAIL", headers, MediaType.APPLICATION_JSON, '{"mailServerHost":"smtp.exmail.qq.com","mailServerPort":465,"mailServerUsername":"Wf@propersoft.cn","mailServerPassword":"9x5qDmxsyrzMra5W","mailServerUseSSL":true,"mailServerDefaultFrom":"Wf@propersoft.cn"}');
        println "response:" + StringUtil.toEncodedString(response.getBody())
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
            + "/rest/notice/server/config/SMS", headers, MediaType.APPLICATION_JSON, '{"smsUrl":"http://118.145.22.173:9887/smsservice/SendSMS","smsTemplate":"UserId=10417&Password=YKrm_180612&Mobiles={0}&Content={1}","smsCharset":"GBK"}');
        println "response:" + StringUtil.toEncodedString(response.getBody())
    }

    @Test
    void configPush() {
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
            + "/rest/notice/server/config/PUSH?pushChannel=XIAOMI", headers, MediaType.APPLICATION_JSON, '{"pushPackage":"com.proper.icmp.dev","appSecret":"RGW+NA+T2ucpEX0a6bxyhA=="}');
        println "response:" + StringUtil.toEncodedString(response.getBody())
    }

    @Test
    void sendEmailAndSmsAndPush() {
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
        noticeSetDocument1.noticeChannel = ["sms","push","email"]
        noticeSetRepository.save(noticeSetDocument1)

        NoticeSetDocument noticeSetDocument2 = new NoticeSetDocument()
        noticeSetDocument2.setUserId("test2")
        noticeSetDocument2.setCatalog("BPM")
        noticeSetDocument2.noticeChannel = ["sms","push","email"]
        noticeSetRepository.save(noticeSetDocument2)

        TemplateEntity templateDocument = new TemplateEntity()
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

        PushDeviceEntity pushDeviceEntity = new PushDeviceEntity()
        pushDeviceEntity.appKey = "MobileOADev"
        pushDeviceEntity.deviceId = "4665a261c995eeab5bac45545aac0b92"
        pushDeviceEntity.userId = "test1"
        pushDeviceEntity.deviceType = PushDeviceType.android
        pushDeviceEntity.deviceOtherInfo = "{\"model\":\"MI 5\",\"manufacturer\":\"Xiaomi\",\"brand\":\"Xiaomi\",\"sdk_int\":24}"
        pushDeviceEntity.pushMode = PushMode.xiaomi
        pushDeviceEntity.pushToken = "w69uYXiVgywg4VE/GJmGnfnomKwfGYs743z09wGK8rjexgJ1hgmmg32O4WpahuFd"
        deviceRepository.save(pushDeviceEntity)
    }

    @After
    void destroy() {
        noticeSetRepository.deleteAll()
        templateRepository.deleteAll()
        deviceRepository.deleteAll()
    }

}
