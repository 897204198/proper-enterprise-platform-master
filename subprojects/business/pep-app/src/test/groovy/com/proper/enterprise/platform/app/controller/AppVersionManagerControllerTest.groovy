package com.proper.enterprise.platform.app.controller

import com.proper.enterprise.platform.app.document.AppVersionDocument
import com.proper.enterprise.platform.app.repository.AppVersionRepository
import com.proper.enterprise.platform.app.service.AppVersionService
import com.proper.enterprise.platform.core.i18n.I18NUtil
import com.proper.enterprise.platform.test.AbstractJPATest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MvcResult

@Sql
class AppVersionManagerControllerTest extends AbstractJPATest {

    @Autowired
    private AppVersionRepository repository
    @Autowired
    private AppVersionService service
    @Autowired
    private CacheManager cacheManager

    def prefix = '/admin/app/versions'

    @Before
    void createDataTest() {
        long version = 300000
        for (int i = 0; i <= 16; i++) {
            AppVersionDocument appVersionDocument = new AppVersionDocument()
            appVersionDocument.setVersion(++version + '')
            appVersionDocument.setNote("第" + version + "版的解释说明")
            appVersionDocument.setAndroidURL("http://test.com/" + version)
            appVersionDocument.setIosURL("itunes://test.com/" + version)
            service.saveOrUpdate(appVersionDocument)
        }
    }

    @Test
    void delete() {
        delete(prefix + "/300010", HttpStatus.NO_CONTENT)

        tearDown()
        cacheManager.getCache('pep.sys.AppVersion').clear()
        delete(prefix + "/300010", HttpStatus.NOT_FOUND)
    }

    @Test
    void updateVersionInfo() {
        long updatever = 300001
        def app = resOfGet("/app/versions/" + updatever, HttpStatus.OK)
        assert !app.forceUpdate
        app.note = "这是更新后的内容信息"
        app.forceUpdate = true
        MvcResult result = put(prefix, JSONUtil.toJSON(app), HttpStatus.OK)
        AppVersionDocument app1 = JSONUtil.parse(result.getResponse().getContentAsString(), AppVersionDocument.class)
        assert app.note == app1.getNote()
        assert app1.getForceUpdate()
    }

    @Test
    void saveThenReleaseThenUpdate() {
        def str = '{"ver":"vt1","androidUrl":".apk","iosUrl":null,"note":"abc"}'
        post(prefix, str, HttpStatus.CREATED)
        assert repository.findByVersion('vt1') != null
        assert !repository.findByVersion('vt1').released

        post("$prefix/latest", str, HttpStatus.CREATED)
        assert repository.findByVersion('vt1').released

        put(prefix, '{"ver":"vt1","androidUrl":"ad.apk","iosUrl":".ipa","note":"abcdef"}', HttpStatus.OK)
        assert repository.findByVersion('vt1').iosURL == '.ipa'
    }

    @Test
    void updateReleaseVersionInfoTest() {
        mockUser('test1', 't1')
        cacheManager.getCache('pep.sys.AppVersion').clear()

        assert resOfGet("/app/versions/latest", HttpStatus.OK) == ''

        AppVersionManagerController.AppVersionVO newreleaseapp = new AppVersionManagerController.AppVersionVO()
        newreleaseapp.setVersion('300005')

        MvcResult releaseapp1 = post(prefix + "/latest", JSONUtil.toJSON(newreleaseapp), HttpStatus.CREATED)
        AppVersionDocument nowapp = JSONUtil.parse(releaseapp1.getResponse().getContentAsString(), AppVersionDocument.class)

        assert nowapp.getVersion() == '300005'
        assert nowapp.getVersion() == newreleaseapp.getVersion()
    }

    @Test
    void list() {
        def v05 = new AppVersionManagerController.AppVersionVO('000500', '*.apk', 'abcdef')
        def v05003 = new AppVersionManagerController.AppVersionVO('0005000003', '*.apk', 'xcv')

        post("$prefix/latest", JSONUtil.toJSON(v05), HttpStatus.CREATED)
        post("$prefix/latest", JSONUtil.toJSON(v05003), HttpStatus.CREATED)

        def list = resOfGet(prefix, HttpStatus.OK)
        assert list.size() > 0
        assert list[0].androidUrl > ''
        assert list[0].iosUrl == null
        assert list[0].ver == '0005000003'
    }

    @Test
    void repeatTest() {
        def v300001 = new AppVersionManagerController.AppVersionVO('300001', '*.apk', 'abcdef')
        assert I18NUtil.getMessage("pep.app.version.repeat") == post("$prefix", JSONUtil.toJSON(v300001), HttpStatus.INTERNAL_SERVER_ERROR)
            .getResponse().getContentAsString()
    }

    @After
    void tearDown() {
        repository.deleteAll()
    }

}
