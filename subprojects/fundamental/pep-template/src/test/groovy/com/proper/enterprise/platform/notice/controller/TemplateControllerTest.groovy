package com.proper.enterprise.platform.notice.controller

import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.template.entity.TemplateEntity
import com.proper.enterprise.platform.template.repository.TemplateRepository
import com.proper.enterprise.platform.template.vo.TemplateDetailVO
import com.proper.enterprise.platform.template.vo.TemplateVO
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

@Sql
class TemplateControllerTest extends AbstractTest {

    @Autowired
    TemplateRepository templateRepository

    @Test
    void getTips() {
        TemplateEntity templateDocument = new TemplateEntity()
        templateDocument.code = "tips"
        templateDocument.name = "name1"
        TemplateDetailVO templateDetailVO = new TemplateDetailVO()
        templateDetailVO.title = "title"
        templateDetailVO.template = "template"
        List<TemplateDetailVO> details = new ArrayList<>()
        details.add(templateDetailVO)
        templateDocument.setDetails(details)
        templateRepository.save(templateDocument)

        def res = get("/template/tips/tips", HttpStatus.OK).getResponse().getContentAsString()
        assert res == "template"
    }

    String saveAndCheck(String code, String name, String catalog, String description, String detailTitle, String detailTemplate, String type) {
        TemplateVO templateVO = new TemplateVO()
        templateVO.code = code
        templateVO.name = name
        templateVO.catalog = catalog
        templateVO.description = description
        TemplateDetailVO detail = new TemplateDetailVO()
        detail.title = detailTitle
        detail.template = detailTemplate
        detail.type = type
        templateVO.detail = detail
        TemplateVO result = JSONUtil.parse(post("/template", JSONUtil.toJSON(templateVO), HttpStatus.CREATED).getResponse().getContentAsString(), TemplateVO.class)
        assert result.id != null
        assert result.code == code
        assert result.name == name
        assert result.catalog == catalog
        assert result.description == description
        assert result.detail.title == detailTitle
        assert result.detail.template == detailTemplate
        assert result.detail.type == type
        return result.id
    }

    void saveAndError(String code, String name, String catalog, String description, String title, String template, String type) {
        TemplateVO templateVO = new TemplateVO()
        templateVO.code = code
        templateVO.name = name
        templateVO.catalog = catalog
        templateVO.description = description
        TemplateDetailVO detail = new TemplateDetailVO()
        detail.title = title
        detail.template = template
        detail.type = type
        templateVO.detail = detail
        post("/template", JSONUtil.toJSON(templateVO), HttpStatus.INTERNAL_SERVER_ERROR)
    }

    String updateAndCheck(String id, String code, String name, String catalog, String description, String detailTitle, String detailTemplate, String type, boolean enable) {
        TemplateVO templateVO = new TemplateVO()
        templateVO.code = code
        templateVO.name = name
        templateVO.catalog = catalog
        templateVO.description = description
        templateVO.enable = enable
        TemplateDetailVO detail = new TemplateDetailVO()
        detail.title = detailTitle
        detail.template = detailTemplate
        detail.type = type
        templateVO.detail = detail
        TemplateVO result = JSONUtil.parse(put("/template/" + id, JSONUtil.toJSON(templateVO), HttpStatus.OK).getResponse().getContentAsString(), TemplateVO.class)
        assert result.id != null
        assert result.code == code
        assert result.name == name
        assert result.catalog == catalog
        assert result.description == description
        assert result.enable == enable
        assert result.detail.title == detailTitle
        assert result.detail.template == detailTemplate
        assert result.detail.type == type
        return result.id
    }

    String disableAndCheck(String id, String code, String name, String catalog, String description, String detailTitle, String detailTemplate, String type, boolean enable) {
        updateAndCheck(id, code, name, catalog, description, detailTitle, detailTemplate, type, enable)
        get("/template/" + id, HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString()
    }

    void updateAndError(String id, String code, String name, String catalog, String description, String title, String template, String type) {
        TemplateVO templateVO = new TemplateVO()
        templateVO.code = code
        templateVO.name = name
        templateVO.catalog = catalog
        templateVO.description = description
        TemplateDetailVO detail = new TemplateDetailVO()
        detail.title = title
        detail.template = template
        detail.type = type
        templateVO.detail = detail
        put("/template/" + id, JSONUtil.toJSON(templateVO), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString()
    }

    void getOneAndCheck(String id, String code, String name, String catalog, String description, String detailTitle, String detailTemplate, String type) {
        TemplateVO result = JSONUtil.parse(get("/template/" + id, HttpStatus.OK).getResponse().getContentAsString(), TemplateVO.class)
        assert result.id == id
        assert result.code == code
        assert result.name == name
        assert result.catalog == catalog
        assert result.description == description
        assert result.detail.title == detailTitle
        assert result.detail.template == detailTemplate
        assert result.detail.type == type
    }

    void deleteAndCheck(String id) {
        delete("/template?ids=" + id, HttpStatus.NO_CONTENT)
        get("/template/" + id, HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString()
    }

    @Test
    void crud() {

        def catalogs = JSONUtil.parse(get("/sys/datadic/catalog/NOTICE_CATALOG", HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert catalogs.size() == 4

        String id = saveAndCheck("CODE1","NAME1","CATALOG1","DESCRIPTION1","TITLE1","TEMPLATE1","TYPE1")
        saveAndError("CODE1","NEW NAME1","NEW CATALOG1","NEW DESCRIPTION1","NEW TITLE1","NEW TEMPLATE1","NEW TYPE1")
        saveAndError(null,"NEW NAME1","NEW CATALOG1","NEW DESCRIPTION1","NEW TITLE1","NEW TEMPLATE1","NEW TYPE1")
        saveAndCheck("CODE2","NAME2","CATALOG2","DESCRIPTION2","TITLE2","TEMPLATE2","TYPE2")
        updateAndCheck(id, "CODE3","NAME3","CATALOG3","DESCRIPTION3","TITLE3","TEMPLATE3","TYPE3", true)
        //disableAndCheck(id, "CODE3","NAME3","CATALOG3","DESCRIPTION3","TITLE3","TEMPLATE3","TYPE3", false)
        updateAndCheck(id, "CODE3","NAME3","CATALOG3","DESCRIPTION3","TITLE3","TEMPLATE3","TYPE3", true)
        updateAndError(id, "CODE2","NAME3","CATALOG3","DESCRIPTION3","TITLE3","TEMPLATE3","TYPE3")
        getOneAndCheck(id, "CODE3","NAME3","CATALOG3","DESCRIPTION3","TITLE3","TEMPLATE3","TYPE3")

        //page
        DataTrunk<TemplateVO> page = JSONUtil.parse(get("/template/" + "?pageNo=1&pageSize=2", HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert page.count == 2
        page = JSONUtil.parse(get("/template/" + "?pageNo=1&pageSize=10&query=NAME", HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert page.count == 2

        deleteAndCheck(id)

        templateRepository.deleteAll()
    }

}
