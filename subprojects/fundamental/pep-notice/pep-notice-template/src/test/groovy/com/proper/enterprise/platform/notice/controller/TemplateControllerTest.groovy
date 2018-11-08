package com.proper.enterprise.platform.notice.controller

import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.notice.entity.TemplateEntity
import com.proper.enterprise.platform.notice.repository.TemplateRepository
import com.proper.enterprise.platform.notice.vo.TemplateVO
import com.proper.enterprise.platform.test.AbstractJPATest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class TemplateControllerTest extends AbstractJPATest {

    @Autowired
    TemplateRepository templateRepository

    @Test
    void getTips() {
        getTipsInit()
        def res = get("/notice/template/tips/tips", HttpStatus.OK).getResponse().getContentAsString()
        assert res == "wow, this is a tips."
    }

    @Test
    void crud() {
        //create
        TemplateVO template = new TemplateVO()
        template.code = "TEST CODE"
        template.template = "TEST TEMPLATE {name} "
        template.title = "TEST TITLE"
        template.name = "TEST NAME"
        template.catelog = "CATELOG1"
        template.type = "PUSH"
        template.description = "name : username"
        TemplateVO result = JSONUtil.parse(post("/notice/template", JSONUtil.toJSON(template), HttpStatus.CREATED).getResponse().getContentAsString(), TemplateVO.class)
        assert result.id != null
        assert result.code == "TEST CODE"
        assert result.template == "TEST TEMPLATE {name} "
        assert result.title == "TEST TITLE"
        assert result.name == "TEST NAME"
        assert result.catelog == "CATELOG1"
        assert result.type == "PUSH"
        assert result.description == "name : username"

        //create same code different noticeType
        template = new TemplateVO()
        template.code = "TEST CODE"
        template.template = "TEST TEMPLATE {name} "
        template.title = "TEST TITLE2"
        template.name = "TEST NAME2"
        template.catelog = "CATELOG2"
        template.type = "EMAIL"
        template.description = "name : username"
        result = JSONUtil.parse(post("/notice/template", JSONUtil.toJSON(template), HttpStatus.CREATED).getResponse().getContentAsString(), TemplateVO.class)
        assert result.id != null
        assert result.code == "TEST CODE"
        assert result.template == "TEST TEMPLATE {name} "
        assert result.title == "TEST TITLE2"
        assert result.name == "TEST NAME2"
        assert result.catelog == "CATELOG2"
        assert result.type == "EMAIL"
        assert result.description == "name : username"

        //create same code same noticeType
        template = new TemplateVO()
        template.code = "TEST CODE"
        template.template = "TEST TEMPLATE {name} "
        template.title = "TEST TITLE2"
        template.name = "TEST NAME2"
        template.catelog = "CATELOG2"
        template.type = "EMAIL"
        template.description = "name : username"
        post("/notice/template", JSONUtil.toJSON(template), HttpStatus.INTERNAL_SERVER_ERROR)

        template = new TemplateVO()
        template.code = "1 1"
        template.template = "1 1 {1} "
        template.title = "1 1"
        template.name = "1 1"
        template.catelog = "1"
        template.type = "1"
        template.description = "1 : 1"
        result = JSONUtil.parse(post("/notice/template", JSONUtil.toJSON(template), HttpStatus.CREATED).getResponse().getContentAsString(), TemplateVO.class)

        //upate same code different noticeType
        String target = result.id
        template = new TemplateVO()
        template.code = "TEST CODE"
        template.template = "TEST TEMPLATE {name} "
        template.title = "TEST TITLE2"
        template.name = "TEST NAME2"
        template.catelog = "CATELOG2"
        template.type = "SMS"
        template.description = "name : username"
        result = JSONUtil.parse(put("/notice/template/" + target, JSONUtil.toJSON(template), HttpStatus.OK).getResponse().getContentAsString(), TemplateVO.class)
        assert result.id != null
        assert result.code == "TEST CODE"
        assert result.template == "TEST TEMPLATE {name} "
        assert result.title == "TEST TITLE2"
        assert result.name == "TEST NAME2"
        assert result.catelog == "CATELOG2"
        assert result.type == "SMS"
        assert result.description == "name : username"

        //upate same code same noticeType , different title
        target = result.id
        template = new TemplateVO()
        template.code = "TEST CODE"
        template.template = "TEST TEMPLATE {name} NEW"
        template.title = "TEST TITLE NEW"
        template.name = "TEST NAME NEW"
        template.catelog = "CATELOG2"
        template.type = "SMS"
        template.description = "name : username"
        result = JSONUtil.parse(put("/notice/template/" + target, JSONUtil.toJSON(template), HttpStatus.OK).getResponse().getContentAsString(), TemplateVO.class)
        assert result.id != null
        assert result.code == "TEST CODE"
        assert result.template == "TEST TEMPLATE {name} NEW"
        assert result.title == "TEST TITLE NEW"
        assert result.name == "TEST NAME NEW"
        assert result.catelog == "CATELOG2"
        assert result.type == "SMS"
        assert result.description == "name : username"

        //upate same code same noticeType
        target = result.id
        template = new TemplateVO()
        template.code = "TEST CODE"
        template.template = "TEST TEMPLATE {name} "
        template.title = "TEST TITLE2"
        template.name = "TEST NAME2"
        template.catelog = "CATELOG2"
        template.type = "EMAIL"
        template.description = "name : username"
        put("/notice/template/" + target, JSONUtil.toJSON(template), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString()

        //getOne
        result = JSONUtil.parse(get("/notice/template/" + target, HttpStatus.OK).getResponse().getContentAsString(), TemplateVO.class)
        assert result.id == target
        assert result.template == "TEST TEMPLATE {name} NEW"
        assert result.title == "TEST TITLE NEW"
        assert result.name == "TEST NAME NEW"
        assert result.catelog == "CATELOG2"
        assert result.type == "SMS"
        assert result.description == "name : username"

        //page
        DataTrunk<TemplateVO> page = JSONUtil.parse(get("/notice/template/" + "?pageNo=1&pageSize=2", HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert page.count == 3

        page = JSONUtil.parse(get("/notice/template/" + "?pageNo=1&pageSize=1&code=TEST CODE", HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert page.count == 3

        //delete
        delete("/notice/template?ids=" + target, HttpStatus.NO_CONTENT)

        //getOne
        get("/notice/template/" + target, HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString()

    }

    void getTipsInit() {
        TemplateEntity templateEntity1 = new TemplateEntity()
        templateEntity1.setCode("tips")
        templateEntity1.setName("name1")
        templateEntity1.setTitle("title1")
        templateEntity1.setTemplate("wow, this is a tips.")
        templateRepository.save(templateEntity1)
        TemplateEntity templateEntity2 = new TemplateEntity()
        templateEntity2.setCode("tips")
        templateEntity2.setName("name2")
        templateEntity2.setTitle("title2")
        templateEntity2.setTemplate("wow, this is a tips.")
        templateRepository.save(templateEntity2)
    }

}
