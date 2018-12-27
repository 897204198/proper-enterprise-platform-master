package com.proper.enterprise.platform.workflow.frame

import com.proper.enterprise.platform.api.auth.service.AccessTokenService
import com.proper.enterprise.platform.core.utils.AntResourceUtil
import com.proper.enterprise.platform.file.entity.FileEntity
import com.proper.enterprise.platform.file.repository.FileRepository
import com.proper.enterprise.platform.notice.server.app.dao.repository.NoticeRepository
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType
import com.proper.enterprise.platform.sys.datadic.DataDic
import com.proper.enterprise.platform.sys.datadic.entity.DataDicEntity
import com.proper.enterprise.platform.sys.datadic.service.DataDicService
import com.proper.enterprise.platform.test.AbstractIntegrationTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockMultipartFile
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

import java.util.concurrent.BlockingQueue

@Ignore
class WorkflowEmailSendTest extends AbstractIntegrationTest {

    private static final String WORKFLOW_EMAIL_SEND_KEY = 'WorkflowEmailSend'

    @Autowired
    FileRepository fileRepository

    @Autowired
    DataDicService dataDicService

    @Autowired
    AccessTokenService accessTokenService

    @Autowired
    NoticeRepository noticeRepository

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor

    @Autowired
    protected WebApplicationContext wac

    protected MockMvc mockMvc

    private String token

    @Before
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()

        String appKey = UUID.randomUUID().toString()
        this.token = post('/auth/login', """{"username":"testuser3","pwd":"123456"}""", HttpStatus.OK).getInputStream().getText()
        def config = [:]
        config.put('mailServerHost', 'smtp.exmail.qq.com')
        config.put('mailServerPort', 465)
        config.put('mailServerUsername', 'Wf@propersoft.cn')
        config.put('mailServerPassword', '9x5qDmxsyrzMra5W')
        config.put('mailServerUseSSL', true)
        config.put('mailServerDefaultFrom', 'Wf@propersoft.cn')
        post("/notice/server/config/" + NoticeType.EMAIL + "/testEmailSendForWF?access_token=" + token, JSONUtil.toJSON(config), HttpStatus.CREATED)
        Map searchConf = resOfGet("/notice/server/config/" + NoticeType.EMAIL + "/testEmailSendForWF?access_token=" + token, HttpStatus.OK)
        assert searchConf.get("mailServerHost") == "smtp.exmail.qq.com"

        DataDic dataDic = new DataDicEntity()
        dataDic.setCatalog("NOTICE_SERVER")
        dataDic.setCode("URL")
        dataDic.setName(getPrefix())
        dataDic.setOrder(0)
        dataDicService.save(dataDic)

        DataDic dataDic2 = new DataDicEntity()
        dataDic2.setCatalog("NOTICE_SERVER")
        dataDic2.setCode("TOKEN")
        dataDic2.setName("testEmailSendForWF")
        dataDic2.setOrder(1)
        dataDicService.save(dataDic2)
    }

    @Test
    @Sql(["/com/proper/enterprise/platform/workflow/identity.sql",
        "/com/proper/enterprise/platform/workflow/datadics.sql",
    "/com/proper/enterprise/platform/workflow/accessToken.sql"])
    void workflowEmailSendTest() {
        Resource[] resourcesWF = AntResourceUtil.getResources("classpath*:bpmn/frame/WorkflowEmailSend.bpmn20.xml")
        String resultWF = mockMvc.perform(
            MockMvcRequestBuilders
                .fileUpload("/file")
                .file(
                new MockMultipartFile("file", "WorkflowEmailSend.bpmn20.xml", ",multipart/form-data", resourcesWF[0].inputStream)
            )
        ).andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn().getResponse().getContentAsString()
        FileEntity fileEntity = fileRepository.findById(resultWF).get()

        Map form1 = new HashMap()
        form1.put("leaveDate_text", "2018年12月26日 12:00:00")
        form1.put("finishDate_text", "2018年12月26日 13:00:00")
        form1.put("vacationHours", "1")
        form1.put("vacationType_text", "事假")
        form1.put("attachment1", fileEntity.getId())
        form1.put("attachment2", fileEntity.getId())
        start(WORKFLOW_EMAIL_SEND_KEY, form1)

        BlockingQueue<Runnable> queue = threadPoolTaskExecutor.getThreadPoolExecutor().getQueue()
        while (queue != null && !queue.isEmpty()) {
            println "sleep 5 milliseconds to wait, current blocking queue is ${queue.size()}"
            sleep(5)
        }
        while (threadPoolTaskExecutor.activeCount > 0) {
            println("sleep 5 milliseconds to wait, current active count is ${threadPoolTaskExecutor.activeCount}")
            sleep(5)
        }
        assert noticeRepository.findAll().size() == 4
        for(def a : noticeRepository.findAll()) {
            assert a.getStatus() == NoticeStatus.SUCCESS
        }
    }

    protected void start(String key, Map<String, Object> form) {
        resOfPost('/workflow/process/' + key + "?access_token=" + this.token, JSONUtil.toJSON(form), HttpStatus.CREATED)
    }
}
