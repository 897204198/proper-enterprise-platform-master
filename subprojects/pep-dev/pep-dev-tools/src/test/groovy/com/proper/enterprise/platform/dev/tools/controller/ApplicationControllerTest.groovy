package com.proper.enterprise.platform.dev.tools.controller

import com.proper.enterprise.platform.app.entity.AppCatalogEntity
import com.proper.enterprise.platform.app.entity.ApplicationEntity
import com.proper.enterprise.platform.app.repository.AppCatalogRepository
import com.proper.enterprise.platform.app.repository.ApplicationRepository
import com.proper.enterprise.platform.app.vo.AppCatalogVO
import com.proper.enterprise.platform.app.vo.ApplicationVO
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class ApplicationControllerTest extends AbstractTest {
    @Autowired
    AppCatalogRepository appCatalogRepo

    @Autowired
    ApplicationRepository applicationRepo

    def prefix = '/admin/app/applications'

    @Test
    void catalogOfMethods() {
        AppCatalogEntity appCatalogDocument = new AppCatalogEntity()
        appCatalogDocument.setId("1")
        appCatalogDocument.setTypeName("问卷调查")
        appCatalogDocument.setCode("category")
        appCatalogRepo.save(appCatalogDocument)
        AppCatalogEntity appCatalogDocument1 = new AppCatalogEntity()
        appCatalogDocument1.setId("2")
        appCatalogDocument1.setTypeName("问卷调查1")
        appCatalogDocument1.setCode("testCategory")
        appCatalogRepo.save(appCatalogDocument1)

        def url1 = "$prefix/catalogs"
        List<AppCatalogVO> catalogVOS = resOfGet(url1, HttpStatus.OK)
        assert catalogVOS.size() == 2
        assert catalogVOS.get(0).code == "testCategory"

        def res = resOfGet(url1 + '/' + appCatalogDocument.getCode(), HttpStatus.OK)
        assert res.code == "category"
        assert res.typeName == "问卷调查"

        AppCatalogEntity appCatalog3 = new AppCatalogEntity()
        appCatalog3.setId("idCatalog")
        appCatalog3.setTypeName("问卷调查问卷调查")
        appCatalog3.setCode("testCate")
        JSONUtil.parse(post(url1, JSONUtil.toJSON(appCatalog3), HttpStatus.CREATED).response.contentAsString,
                AppCatalogVO.class)
        List<AppCatalogVO> list = resOfGet(url1, HttpStatus.OK)
        assert list.size() == 3
        assert list.get(0).code == "testCate"

        String typeName = "modifyTypeName"
        put(url1 + '/' + appCatalog3.getCode() + '?typeName=modifyTypeName', typeName, HttpStatus.OK)
        List list1 = appCatalogRepo.findAll()
        list1.get(0).typeName == 'modifyTypeName'

        deleteAndReturn("/admin/app/applications/catalogs", appCatalog3.code, HttpStatus.NO_CONTENT)
        List<AppCatalogVO> deleteList = resOfGet(url1, HttpStatus.OK)
        assert deleteList.size() == 2
        assert deleteList.get(0).code == "testCategory"
    }

    @Test
    void applicationOfMethods() {
        ApplicationEntity applicationEntity = new ApplicationEntity()
        applicationEntity.setId("1111")
        applicationEntity.setCode("category")
        applicationEntity.setName("问卷调查")
        applicationEntity.setPage("examList2")
        applicationEntity.setIcon("./assets/images/application.png")
        applicationEntity.setStyle("style")
        post(prefix, JSONUtil.toJSON(applicationEntity), HttpStatus.CREATED)

        List<ApplicationEntity> list = applicationRepo.findAll()
        assert list.get(0).name == "问卷调查"

        resOfGet(prefix, HttpStatus.OK).size() == 1
        resOfGet(prefix + "?code=category", HttpStatus.OK).size() == 1

        ApplicationEntity application = new ApplicationEntity()
        application.setCode("category")
        application.setName("问卷调查")
        application.setPage("examList2")
        application.setIcon("./assets/images/application.png")
        application.setStyle("style")
        application.setIcon('test_icon_change')
        applicationRepo.save(application)
        String id = application.getId()
        def res = JSONUtil.parse(put('/admin/app/applications/' + id, JSONUtil.toJSON(application), HttpStatus.OK)
                .getResponse().getContentAsString(), ApplicationVO.class)
        assert res.icon == 'test_icon_change'
        assert res.name == '问卷调查'

        get(prefix + "/" + id, HttpStatus.OK)
    }
}
