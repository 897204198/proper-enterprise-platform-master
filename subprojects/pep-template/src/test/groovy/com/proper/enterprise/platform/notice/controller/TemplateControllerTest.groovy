package com.proper.enterprise.platform.notice.controller

import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.template.entity.TemplateEntity
import com.proper.enterprise.platform.template.repository.TemplateRepository
import com.proper.enterprise.platform.template.vo.TemplateVO
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class TemplateControllerTest extends AbstractTest {

    @Autowired
    TemplateRepository templateRepository

    @Test
    void getTips() {
        getTipsInit()
        def res = get("/notice/template/tips/tips", HttpStatus.OK).getResponse().getContentAsString()
        assert res == "push template"
    }

    @Test
    void crud() {
        //create
        TemplateVO template = new TemplateVO()
        template.code = "TEST CODE"
        template.pushTemplate = "TEST TEMPLATE {name} "
        template.pushTitle = "TEST TITLE"
        template.smsTemplate = 'TEST TEMPLATE {name}'
        template.smsTitle = 'TEST TITLE'
        template.setEmailTemplate('TEST TEMPLATE {name}')
        template.setEmailTitle('TEST TITLE')
        template.name = "TEST NAME"
        template.catelog = "CATELOG1"
        template.description = "name : username"
        TemplateVO result = JSONUtil.parse(post("/notice/template", JSONUtil.toJSON(template), HttpStatus.CREATED).getResponse().getContentAsString(), TemplateVO.class)
        assert result.id != null
        assert result.code == "TEST CODE"
        assert result.pushTemplate == "TEST TEMPLATE {name} "
        assert result.pushTitle == "TEST TITLE"
        assert result.name == "TEST NAME"
        assert result.catelog == "CATELOG1"
        assert result.description == "name : username"
        assert result.smsTemplate == 'TEST TEMPLATE {name}'
        assert result.smsTitle == 'TEST TITLE'
        assert result.emailTemplate == 'TEST TEMPLATE {name}'
        assert result.emailTitle == 'TEST TITLE'
        def id = result.id

        //create same code
        template = new TemplateVO()
        template.code = "TEST CODE"
        template.pushTemplate = "TEST TEMPLATE {name} "
        template.pushTitle = "TEST TITLE2"
        template.name = "TEST NAME2"
        template.catelog = "CATELOG2"
        template.description = "name : username"
        template.smsTemplate = 'TEST TEMPLATE {name}'
        template.smsTitle = 'TEST TITLE'
        template.emailTemplate = 'TEST TEMPLATE {name}'
        template.emailTitle = 'TEST TITLE'
        post("/notice/template", JSONUtil.toJSON(template), HttpStatus.INTERNAL_SERVER_ERROR)

        template = new TemplateVO()
        template.code = "1 1"
        template.pushTemplate = "1 1 {1} "
        template.pushTitle = "1 1"
        template.name = "1 1"
        template.catelog = "1"
        template.description = "1 : 1"
        template.smsTemplate = 'TEST TEMPLATE {name}'
        template.smsTitle = 'TEST TITLE'
        template.emailTemplate = 'TEST TEMPLATE {name}'
        template.emailTitle = 'TEST TITLE'
        result = JSONUtil.parse(post("/notice/template", JSONUtil.toJSON(template), HttpStatus.CREATED).getResponse().getContentAsString(), TemplateVO.class)

        //success upate
        String target = id
        template = new TemplateVO()
        template.code = "TEST CODE"
        template.pushTemplate = "TEST TEMPLATE {name} "
        template.pushTitle = "TEST TITLE2"
        template.name = "TEST NAME2"
        template.catelog = "CATELOG2"
        template.description = "name : username"
        template.smsTemplate = 'TEST TEMPLATE {name}'
        template.smsTitle = 'TEST TITLE'
        template.emailTemplate = 'TEST TEMPLATE {name}'
        template.emailTitle = 'TEST TITLE'
        result = JSONUtil.parse(put("/notice/template/" + id, JSONUtil.toJSON(template), HttpStatus.OK).getResponse().getContentAsString(), TemplateVO.class)
        assert result.id == id
        assert result.code == "TEST CODE"
        assert result.pushTemplate == "TEST TEMPLATE {name} "
        assert result.pushTitle == "TEST TITLE2"
        assert result.name == "TEST NAME2"
        assert result.catelog == "CATELOG2"
        assert result.description == "name : username"
        assert result.smsTemplate == 'TEST TEMPLATE {name}'
        assert result.smsTitle == 'TEST TITLE'
        assert result.emailTemplate == 'TEST TEMPLATE {name}'
        assert result.emailTitle == 'TEST TITLE'

        //error upate
        target = id
        template = new TemplateVO()
        template.code = "1 1"
        template.pushTemplate = "TEST TEMPLATE {name} "
        template.pushTitle = "TEST TITLE2"
        template.name = "TEST NAME2"
        template.catelog = "CATELOG2"
        template.description = "name : username"
        template.smsTemplate = 'TEST TEMPLATE {name}'
        template.smsTitle = 'TEST TITLE'
        template.emailTemplate = 'TEST TEMPLATE {name}'
        template.emailTitle = 'TEST TITLE'
        put("/notice/template/" + id, JSONUtil.toJSON(template), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString()

        //getOne
        result = JSONUtil.parse(get("/notice/template/" + id, HttpStatus.OK).getResponse().getContentAsString(), TemplateVO.class)
        assert result.id == target
        assert result.pushTemplate == "TEST TEMPLATE {name} "
        assert result.pushTitle == "TEST TITLE2"
        assert result.name == "TEST NAME2"
        assert result.catelog == "CATELOG2"
        assert result.description == "name : username"
        assert result.smsTemplate == 'TEST TEMPLATE {name}'
        assert result.smsTitle == 'TEST TITLE'
        assert result.emailTemplate == 'TEST TEMPLATE {name}'
        assert result.emailTitle == 'TEST TITLE'

        //page
        DataTrunk<TemplateVO> page = JSONUtil.parse(get("/notice/template/" + "?pageNo=1&pageSize=2", HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert page.count == 2

        page = JSONUtil.parse(get("/notice/template/" + "?pageNo=1&pageSize=1&code=TEST CODE", HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert page.count == 1

        //delete
        delete("/notice/template?ids=" + target, HttpStatus.NO_CONTENT)

        //getOne
        get("/notice/template/" + target, HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString()

    }

    void getTipsInit() {
        TemplateEntity templateEntity1 = new TemplateEntity()
        templateEntity1.setCode("tips")
        templateEntity1.setName("name1")
        templateEntity1.setSmsTitle("sms title")
        templateEntity1.setSmsTemplate("sms template")
        templateEntity1.setPushTitle("push title")
        templateEntity1.setPushTemplate("push template")
        templateEntity1.setSmsTitle("email title")
        templateEntity1.setSmsTemplate("email template")
        templateRepository.save(templateEntity1)
    }

}
