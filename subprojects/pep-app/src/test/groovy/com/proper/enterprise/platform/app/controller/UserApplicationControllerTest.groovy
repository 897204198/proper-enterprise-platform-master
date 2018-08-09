package com.proper.enterprise.platform.app.controller

import com.proper.enterprise.platform.app.entity.AppCatalogEntity
import com.proper.enterprise.platform.app.entity.ApplicationEntity
import com.proper.enterprise.platform.app.entity.UserApplicationEntity
import com.proper.enterprise.platform.app.repository.AppCatalogRepository
import com.proper.enterprise.platform.app.repository.ApplicationRepository
import com.proper.enterprise.platform.app.repository.UserApplicationsRepository
import com.proper.enterprise.platform.auth.common.jpa.entity.UserEntity
import com.proper.enterprise.platform.auth.common.jpa.repository.UserRepository
import com.proper.enterprise.platform.core.security.Authentication
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class UserApplicationControllerTest extends AbstractTest {

    @Autowired
    UserApplicationsRepository userApplicationsRepo

    @Autowired
    ApplicationRepository applicationRepo

    @Autowired
    UserRepository userRepository

    @Autowired
    AppCatalogRepository appCatalogRepo

    @Test
    void testAllApplications() {
        List list = resOfGet("/app/applications/all", HttpStatus.OK)
        assert list.size() == 1
        assert list.get(0).code == 'category'
        assert list.get(0).typeName == '问卷调查'
        list.get(0).apps.size() == 2
        list.get(0).apps.get(0).name == "问卷调查"
        list.get(0).apps.get(1).name == "调查调查"
    }

    @Test
    void getUserApplications() {
        Authentication.setCurrentUserId('PEP_SYS')
        List list = resOfGet("/app/applications", HttpStatus.OK)
        assert list.size() == 1
        assert list.get(0).name == '问卷调查'
        assert list.get(0).icon == './assets/images/application.png'
        assert list.get(0).page == 'examList'
    }

    @Test
    void testUpdateIds() {
        def updateIds = ['ids':'001,002']
        UserApplicationEntity result = JSONUtil.parse(put('/app/applications', JSONUtil.toJSON(updateIds), HttpStatus.OK).
                getResponse().getContentAsString(), UserApplicationEntity.class)
        assert result.appId == '001,002'
    }

    @Before
    void init() {
        UserEntity userInfo = new UserEntity()
        userInfo.setName("user")
        userInfo.setPassword("pwd")
        userInfo.setPhone("13900001234")
        userInfo.setId("PEP_SYS")
        userInfo.setUsername("username")
        userInfo.setPassword("123456")
        userRepository.save(userInfo)

        AppCatalogEntity appCatalogDocument = new AppCatalogEntity()
        appCatalogDocument.setId("id")
        appCatalogDocument.setCode("category")
        appCatalogDocument.setTypeName("问卷调查")
        appCatalogRepo.save(appCatalogDocument)

        ApplicationEntity appDoc1 = new ApplicationEntity()
        appDoc1.setCode("category")
        appDoc1.setName("问卷调查")
        appDoc1.setIcon("./assets/images/application.png")
        appDoc1.setPage("examList")
        appDoc1.setData("'url': 'http://192.168.1.111/icmp/desktop/#/customframe/exam-list'")
        applicationRepo.save(appDoc1)

        ApplicationEntity appDoc2 = new ApplicationEntity()
        appDoc2.setCode("category")
        appDoc2.setName("调查调查")
        appDoc2.setIcon("./assets/images/application.png")
        appDoc2.setPage("examList")
        appDoc2.setData("121")
        applicationRepo.save(appDoc2)

        UserApplicationEntity userApplicationDocument = new UserApplicationEntity()
        userApplicationDocument.setUserId(userInfo.getId())
        userApplicationDocument.setAppId(appDoc1.getId())
        userApplicationsRepo.save(userApplicationDocument)
    }

    @After
    void after() {
        userApplicationsRepo.deleteAll()
        appCatalogRepo.deleteAll()
    }

}
