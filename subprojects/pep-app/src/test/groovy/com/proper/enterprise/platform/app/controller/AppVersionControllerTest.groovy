package com.proper.enterprise.platform.app.controller

import com.proper.enterprise.platform.app.document.AppVersionDocument
import com.proper.enterprise.platform.app.repository.AppVersionRepository
import com.proper.enterprise.platform.app.service.AppVersionService
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MvcResult

class AppVersionControllerTest extends AbstractTest {

    @Autowired
    private AppVersionRepository repository
    @Autowired
    private AppVersionService appVersionService
    @Autowired
    private CacheManager cacheManager

    def prefix = '/sys/app/versions'

    @Before
    void createDataTest() {
        for (int version = 300000; version <= 300016; version++) {
            AppVersionDocument appVersionDocument = new AppVersionDocument()
            appVersionDocument.setVer(version)
            appVersionDocument.setNote("第" + version + "版的解释说明")
            appVersionDocument.setUrl("http://test.com/" + version)
            appVersionService.save(appVersionDocument)
        }
    }

    @Test
    void getLatestReleaseVersionInfoByVer() {
        cacheManager.getCache('pep.sys.AppVersion').clear()
        def url = "$prefix/latest"
        MvcResult result = get(url, HttpStatus.OK)
        AppVersionDocument app = JSONUtil.parse(result.getResponse().getContentAsString(), AppVersionDocument.class)
        assert app.getVer() == 300016

        resOfGet("$prefix/latest?current=300015", HttpStatus.OK).ver == 30016
    }

    @Test
    void getCertainVersionInfo() {
        def url = "$prefix/300016"
        MvcResult result = get(url, HttpStatus.OK)
        AppVersionDocument app = JSONUtil.parse(result.getResponse().getContentAsString(), AppVersionDocument.class)
        assert app.getVer() == 300016
    }

    @After
    void tearDown() {
        repository.deleteAll()
    }

}
