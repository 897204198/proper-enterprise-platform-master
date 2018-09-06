package com.proper.enterprise.platform.notice.controller

import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.sys.datadic.DataDicLiteBean
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
        assert res == "template"
    }

    @Test
    void crud() {
        //create
        TemplateVO template = new TemplateVO()
        template.code = "TEST CODE"
        template.template = "TEST TEMPLATE {name} "
        template.title = "TEST TITLE"
        template.name = "TEST NAME"
        template.catalog = "CATALOG1"
        DataDicLiteBean type = new DataDicLiteBean()
        type.setCode("TEST")
        type.setCatalog("TEMPLATE_TYPE")
        template.type = type
        template.description = "name : username"
        TemplateVO result = JSONUtil.parse(post("/notice/template", JSONUtil.toJSON(template), HttpStatus.CREATED).getResponse().getContentAsString(), TemplateVO.class)
        assert result.id != null
        assert result.code == "TEST CODE"
        assert result.template == "TEST TEMPLATE {name} "
        assert result.title == "TEST TITLE"
        assert result.name == "TEST NAME"
        assert result.catalog == "CATALOG1"
        assert result.description == "name : username"
        def id = result.id

        //create same code
        template = new TemplateVO()
        template.code = "TEST CODE"
        template.template = "TEST TEMPLATE {name} "
        template.title = "TEST TITLE2"
        template.name = "TEST NAME2"
        template.catalog = "CATALOG1"
        type.setCode("TEST")
        type.setCatalog("TEMPLATE_TYPE")
        template.type = type
        template.description = "name : username"
        post("/notice/template", JSONUtil.toJSON(template), HttpStatus.INTERNAL_SERVER_ERROR)

        template = new TemplateVO()
        template.code = "1 1"
        template.template = "1 1 {1} "
        template.title = "1 1"
        template.name = "1 1"
        template.catalog = "1"
        template.description = "1 : 1"
        result = JSONUtil.parse(post("/notice/template", JSONUtil.toJSON(template), HttpStatus.CREATED).getResponse().getContentAsString(), TemplateVO.class)

        //success upate
        String target = id
        template = new TemplateVO()
        template.code = "TEST CODE"
        template.template = "TEST TEMPLATE {name} "
        template.title = "TEST TITLE2"
        template.name = "TEST NAME2"
        template.catalog = "CATALOG2"
        type.setCode("TEST")
        type.setCatalog("TEMPLATE_TYPE")
        template.type = type
        template.description = "name : username"
        result = JSONUtil.parse(put("/notice/template/" + id, JSONUtil.toJSON(template), HttpStatus.OK).getResponse().getContentAsString(), TemplateVO.class)
        assert result.id == id
        assert result.code == "TEST CODE"
        assert result.template == "TEST TEMPLATE {name} "
        assert result.title == "TEST TITLE2"
        assert result.name == "TEST NAME2"
        assert result.catalog == "CATALOG2"
        assert result.description == "name : username"

        //error upate
        target = id
        template = new TemplateVO()
        template.code = "1 1"
        template.template = "TEST TEMPLATE {name} "
        template.title = "TEST TITLE2"
        template.name = "TEST NAME2"
        template.catalog = "CATALOG2"
        template.description = "name : username"
        put("/notice/template/" + id, JSONUtil.toJSON(template), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString()

        //getOne
        result = JSONUtil.parse(get("/notice/template/" + id, HttpStatus.OK).getResponse().getContentAsString(), TemplateVO.class)
        assert result.id == target
        assert result.template == "TEST TEMPLATE {name} "
        assert result.title == "TEST TITLE2"
        assert result.name == "TEST NAME2"
        assert result.catalog == "CATALOG2"
        assert result.description == "name : username"

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
        templateEntity1.setTitle("title")
        templateEntity1.setTemplate("template")
        templateRepository.save(templateEntity1)
    }

}
