package com.proper.enterprise.platform.app.controller

import com.proper.enterprise.platform.app.document.AppVersionDocument
import com.proper.enterprise.platform.app.repository.AppVersionRepository
import com.proper.enterprise.platform.app.service.AppVersionService
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager
import org.springframework.http.HttpStatus

class AppVersionControllerTest extends AbstractTest {

    @Autowired
    private AppVersionRepository repository
    @Autowired
    private AppVersionService service
    @Autowired
    private CacheManager cacheManager

    def prefix = '/app/versions'

    @Before
    void createDataTest() {
        for (int version = 300000; version <= 300016; version++) {
            AppVersionDocument appVersionDocument = new AppVersionDocument()
            appVersionDocument.setVersion(version + '')
            appVersionDocument.setNote("第" + version + "版的解释说明")
            appVersionDocument.setAndroidURL("http://test.com/" + version)
            appVersionDocument.setIosURL("itunes://test.com/" + version)
            service.saveOrUpdate(appVersionDocument)
        }
    }

    @Test
    void getLatestReleaseVersionInfoByVer() {
        cacheManager.getCache('pep.sys.AppVersion').clear()

        service.release(service.get('300016'))

        def url = "$prefix/latest"
        def app = resOfGet(url, HttpStatus.OK)
        assert app.ver == '300016'

        assert resOfGet("$prefix/latest?current=300015", HttpStatus.OK).ver == '300016'
    }

    @Test
    void getCertainVersionInfo() {
        def url = "$prefix/300016"
        def app = resOfGet(url, HttpStatus.OK)
        assert app.ver == '300016'
    }

    @After
    void tearDown() {
        repository.deleteAll()
    }

}
