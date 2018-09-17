package com.proper.enterprise.platform.notice.controller

import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.template.repository.TemplateRepository
import com.proper.enterprise.platform.template.vo.TemplateDetailVO
import com.proper.enterprise.platform.template.vo.TemplateVO
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class TemplatesControllerTest extends AbstractTest {

    @Autowired
    TemplateRepository templateRepository

    String saveAndCheck(String code, String name, String catalog, String description, String detailTitle, String detailTemplate, String type) {
        TemplateVO templateVO = new TemplateVO()
        templateVO.code = code
        templateVO.name = name
        templateVO.catalog = catalog
        templateVO.description = description
        List<TemplateDetailVO> details = new ArrayList<>()
        TemplateDetailVO detail = new TemplateDetailVO()
        detail.title = detailTitle
        detail.template = detailTemplate
        detail.type = type
        details.add(detail)
        templateVO.details = details
        TemplateVO result = JSONUtil.parse(post("/templates", JSONUtil.toJSON(templateVO), HttpStatus.CREATED).getResponse().getContentAsString(), TemplateVO.class)
        assert result.id != null
        assert result.code == code
        assert result.name == name
        assert result.catalog == catalog
        assert result.description == description
        assert result.details[0].title == detailTitle
        assert result.details[0].template == detailTemplate
        assert result.details[0].type == type
        return result.id
    }

    void saveAndError(String code, String name, String catalog, String description, String type) {
        TemplateVO templateVO = new TemplateVO()
        templateVO.code = code
        templateVO.name = name
        templateVO.catalog = catalog
        templateVO.description = description
        List<TemplateDetailVO> details = new ArrayList<>()
        TemplateDetailVO detail = new TemplateDetailVO()
        detail.type = type
        details.add(detail)
        templateVO.details = details
        post("/templates", JSONUtil.toJSON(templateVO), HttpStatus.INTERNAL_SERVER_ERROR)
    }

    String updateAndCheck(String id, String code, String name, String catalog, String description, String detailTitle, String detailTemplate, String type) {
        TemplateVO templateVO = new TemplateVO()
        templateVO.code = code
        templateVO.name = name
        templateVO.catalog = catalog
        templateVO.description = description
        List<TemplateDetailVO> details = new ArrayList<>()
        TemplateDetailVO detail = new TemplateDetailVO()
        detail.title = detailTitle
        detail.template = detailTemplate
        detail.type = type
        details.add(detail)
        templateVO.details = details
        TemplateVO result = JSONUtil.parse(put("/templates/" + id, JSONUtil.toJSON(templateVO), HttpStatus.OK).getResponse().getContentAsString(), TemplateVO.class)
        assert result.id != null
        assert result.code == code
        assert result.name == name
        assert result.catalog == catalog
        assert result.description == description
        assert result.details[0].title == detailTitle
        assert result.details[0].template == detailTemplate
        assert result.details[0].type == type
        return result.id
    }

    void updateAndError(String id, String code, String name, String catalog, String description, String type) {
        TemplateVO templateVO = new TemplateVO()
        templateVO.code = code
        templateVO.name = name
        templateVO.catalog = catalog
        templateVO.description = description
        List<TemplateDetailVO> details = new ArrayList<>()
        TemplateDetailVO detail = new TemplateDetailVO()
        detail.type = type
        details.add(detail)
        templateVO.details = details
        put("/templates/" + id, JSONUtil.toJSON(templateVO), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString()
    }

    void getOneAndCheck(String id, String code, String name, String catalog, String description, String detailTitle, String detailTemplate, String type) {
        TemplateVO result = JSONUtil.parse(get("/templates/" + id, HttpStatus.OK).getResponse().getContentAsString(), TemplateVO.class)
        assert result.id == id
        assert result.code == code
        assert result.name == name
        assert result.catalog == catalog
        assert result.description == description
        assert result.details[0].title == detailTitle
        assert result.details[0].template == detailTemplate
        assert result.details[0].type == type
    }

    void deleteAndCheck(String id) {
        delete("/templates?ids=" + id, HttpStatus.NO_CONTENT)
        get("/templates/" + id, HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString()
    }

    @Test
    void crud() {
        String id = saveAndCheck("CODE1","NAME1","CATALOG1","DESCRIPTION1","TITLE1","TEMPLATE1","TYPE1")
        saveAndError("CODE1","NEW NAME1","NEW CATALOG1","NEW DESCRIPTION1","NEW TYPE1")
        saveAndError(null,"NEW NAME1","NEW CATALOG1","NEW DESCRIPTION1","NEW TYPE1")
        saveAndCheck("CODE2","NAME2","CATALOG2","DESCRIPTION2","TITLE2","TEMPLATE2","TYPE2")
        updateAndCheck(id, "CODE3","NAME3","CATALOG3","DESCRIPTION3","TITLE3","TEMPLATE3","TYPE3")
        updateAndError(id, "CODE2","NAME3","CATALOG3","DESCRIPTION3","TYPE3")
        getOneAndCheck(id, "CODE3","NAME3","CATALOG3","DESCRIPTION3","TITLE3","TEMPLATE3","TYPE3")

        //page
        DataTrunk<TemplateVO> page = JSONUtil.parse(get("/templates/" + "?pageNo=1&pageSize=2", HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert page.count == 2
        page = JSONUtil.parse(get("/templates/" + "?pageNo=1&pageSize=10&query=NAME", HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert page.count == 2

        deleteAndCheck(id)

        templateRepository.deleteAll()
    }

}
