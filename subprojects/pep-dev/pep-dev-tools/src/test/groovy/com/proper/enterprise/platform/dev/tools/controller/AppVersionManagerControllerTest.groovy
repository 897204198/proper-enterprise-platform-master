package com.proper.enterprise.platform.dev.tools.controller

import com.proper.enterprise.platform.app.document.AppVersionDocument
import com.proper.enterprise.platform.app.repository.AppVersionRepository
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MvcResult

class AppVersionManagerControllerTest extends AbstractTest {

    @Autowired
    private AppVersionRepository repository
    @Autowired
    private CacheManager cacheManager

    def prefix = '/admin/app/versions'

    @Before
    void createDataTest() {
        long version = 300000
        for (int i = 0; i <= 16; i++) {
            AppVersionDocument appVersionDocument = new AppVersionDocument()
            appVersionDocument.setVer(++version)
            appVersionDocument.setNote("第" + version + "版的解释说明")
            appVersionDocument.setUrl("http://test.com/" + version)
            MvcResult result = post(prefix, JSONUtil.toJSON(appVersionDocument), HttpStatus.CREATED)
            AppVersionDocument reapp = JSONUtil.parse(result.getResponse().getContentAsString(), AppVersionDocument.class)
            assert appVersionDocument.getVer() == reapp.getVer()
        }
    }

    @Test
    void invalid() {
        delete(prefix + "/300010", HttpStatus.NO_CONTENT)
    }

    @Test
    void getVersionInfosByPageVersionNull() {
        MvcResult result = get(prefix + "?pageNo=1&pageSize=10", HttpStatus.OK)
        Map<String, Object> map = JSONUtil.parse(result.getResponse().getContentAsString(), Map.class)
        List<AppVersionDocument> list = (List<AppVersionDocument>) map.get("data")
        assert map.get("count") == 17
        assert list.size() == 10
    }

    @Test
    void getgetVersionInfosByPageVersionNotNull() {
        MvcResult result = get(prefix + "?version=300005&pageNo=1&pageSize=10", HttpStatus.OK)
        Map<String, Object> map = JSONUtil.parse(result.getResponse().getContentAsString(), Map.class)
        List<AppVersionDocument> list = (List<AppVersionDocument>) map.get("data")
        assert map.get("count") == 1
        assert list.size() == 1
    }

    @Test
    void getVersionInfosByNoPage() {
        MvcResult result = get(prefix, HttpStatus.OK)
        Map<String, Object> map = JSONUtil.parse(result.getResponse().getContentAsString(), Map.class)
        assert map.get("count") == 17
    }

    @Test
    void updateVersionInfo() {
        long updatever = 300001
        MvcResult appresult = get("/sys/app/versions/" + updatever, HttpStatus.OK)
        AppVersionDocument app = JSONUtil.parse(appresult.getResponse().getContentAsString(), AppVersionDocument.class)
        app.setNote("这是更新后的内容信息")
        MvcResult result = put("$prefix/" + updatever, JSONUtil.toJSON(app), HttpStatus.OK)
        AppVersionDocument app1 = JSONUtil.parse(result.getResponse().getContentAsString(), AppVersionDocument.class)
        assert app.getNote() == app1.getNote()
    }

    @Test
    void updateReleaseVersionInfoTest() {
        cacheManager.getCache('pep.sys.AppVersion').clear()
        MvcResult releaseapp = get("/sys/app/versions/latest", HttpStatus.OK)
        AppVersionDocument originalapp = JSONUtil.parse(releaseapp.getResponse().getContentAsString(), AppVersionDocument.class)

        AppVersionDocument newreleaseapp = new AppVersionDocument()
        newreleaseapp.setVer(300005)

        MvcResult releaseapp1 = put(prefix + "/latest", JSONUtil.toJSON(newreleaseapp), HttpStatus.OK)
        AppVersionDocument nowapp = JSONUtil.parse(releaseapp1.getResponse().getContentAsString(), AppVersionDocument.class)

        assert originalapp.getVer() != nowapp.getVer()
        assert nowapp.getVer() == newreleaseapp.getVer()
    }

    @Test
    void initGetLatestVersion() {
        tearDown()
        resOfGet("$prefix/latest", HttpStatus.OK)
    }

    @After
    void tearDown() {
        repository.deleteAll()
    }

}
