package com.proper.enterprise.platform.dev.tools.controller

import com.proper.enterprise.platform.api.cache.CacheDuration
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.http.HttpStatus

@CacheDuration(cacheName = "CMCT")
class CacheManagerControllerTest extends AbstractTest {

    @Autowired
    private CacheManager cacheManager

    def static final CACHE_NAME = 'CMCT'

    def static final CACHE_NAME1 = 'CMCT1'
    def static final CACHE_NAME2 = 'CMCT2'

    @CacheDuration(cacheName = "CMCT1")
    initCache1(){}

    @CacheDuration(cacheName = "CMCT2")
    initCache2(){}

    @Before
    void setUp() {
        initCache1()
        initCache2()

        List<String> lists = new ArrayList<>(3)
        lists.add("a")
        lists.add("b")
        lists.add("c")
        Cache cache = cacheManager.getCache(CACHE_NAME)
        cache.put('test-k1', 'string value')
        cache.put('test-k2', ['a': 'ABC'])
        cache.put('lists', lists)

        Cache cache1 = cacheManager.getCache(CACHE_NAME1)
        cache1.put("k11","k11 value")

        Cache cache2 = cacheManager.getCache(CACHE_NAME2)
        cache2.put("k22","k22 value")
        cache2.put("k223","k223 value")
    }

    @After
    void tearDown() {
        cacheManager.getCache(CACHE_NAME).clear()
    }

    @Test
    void testGet() {
        resOfGet('/admin/dev/cache', HttpStatus.OK).contains(CACHE_NAME)
        assert resOfGet("/admin/dev/cache/tempcachename/test-k1", HttpStatus.OK) == ''
        assert resOfGet("/admin/dev/cache/$CACHE_NAME/test-k1", HttpStatus.OK) == 'string value'
        assert resOfGet("/admin/dev/cache/$CACHE_NAME/test-k1?className=java.lang.String", HttpStatus.OK) == 'string value'
        assert get("/admin/dev/cache/$CACHE_NAME/test-k2?className=java.util.Map", HttpStatus.OK).getResponse().getContentAsString() == '{"a":"ABC"}'
        assert get("/admin/dev/cache/$CACHE_NAME/lists?className=java.util.List", HttpStatus.OK).getResponse().getContentAsString() == '["a","b","c"]'
    }

    @Test
    void testCleanCache() {
        delete("/admin/dev/cache/tempcachename", HttpStatus.NO_CONTENT)
        assert resOfGet("/admin/dev/cache/$CACHE_NAME/test-k1", HttpStatus.OK) == 'string value'
        delete("/admin/dev/cache/$CACHE_NAME", HttpStatus.NO_CONTENT)
        assert resOfGet("/admin/dev/cache/$CACHE_NAME/test-k1", HttpStatus.OK) == ''
    }

    @Test
    void testCleanCacheWithKey() {
        assert resOfGet("/admin/dev/cache/$CACHE_NAME/test-k1", HttpStatus.OK) == 'string value'
        delete("/admin/dev/cache/$CACHE_NAME/test-k1", HttpStatus.NO_CONTENT)
        assert resOfGet("/admin/dev/cache/$CACHE_NAME/test-k1", HttpStatus.OK) == ''
    }

    @Test
    void testListCacheKeys() {
        def list = resOfGet("/admin/dev/cache/$CACHE_NAME", HttpStatus.OK)
        assert list.size == 3
        assert list[0] == 'lists'
        assert list[1] == 'test-k1'
        assert list[2] == 'test-k2'

        list = resOfGet("/admin/dev/cache/$CACHE_NAME"+"?pageNo=1&pageSize=5", HttpStatus.OK)
        assert list.size == 3
        assert list[0] == 'lists'
        assert list[1] == 'test-k1'
        assert list[2] == 'test-k2'

        list = resOfGet("/admin/dev/cache/$CACHE_NAME"+"?pageNo=2&pageSize=2", HttpStatus.OK)
        assert list.size == 1
        assert list[0] == 'test-k2'

        list = resOfGet("/admin/dev/cache/$CACHE_NAME"+"?pageNo=-2", HttpStatus.OK)
        assert list.size == 3
        assert list[0] == 'lists'
        assert list[1] == 'test-k1'
        assert list[2] == 'test-k2'
    }

    @Test
    void testDeleteInBatches(){
        String names = "CMCT1,CMCT2"
        assert resOfGet("/admin/dev/cache/$CACHE_NAME1/k11", HttpStatus.OK) == 'k11 value'
        assert resOfGet("/admin/dev/cache/$CACHE_NAME2/k22", HttpStatus.OK) == 'k22 value'
        delete("/admin/dev/cache?names="+names, HttpStatus.NO_CONTENT)
        assert resOfGet("/admin/dev/cache/$CACHE_NAME1/k11", HttpStatus.OK) == ''
        assert resOfGet("/admin/dev/cache/$CACHE_NAME2/k22", HttpStatus.OK) == ''
    }

    @Test
    void testDeleteCacheOrKeyByName(){
        String key = "k22,k223"
        assert resOfGet("/admin/dev/cache/$CACHE_NAME1/k11", HttpStatus.OK) == 'k11 value'
        delete("/admin/dev/cache/$CACHE_NAME1", HttpStatus.NO_CONTENT)
        assert resOfGet("/admin/dev/cache/$CACHE_NAME1/k11", HttpStatus.OK) == ''

        assert resOfGet("/admin/dev/cache/$CACHE_NAME2/k22", HttpStatus.OK) == 'k22 value'
        assert resOfGet("/admin/dev/cache/$CACHE_NAME2/k223", HttpStatus.OK) == 'k223 value'
        delete("/admin/dev/cache/$CACHE_NAME2?key="+key, HttpStatus.NO_CONTENT)
        assert resOfGet("/admin/dev/cache/$CACHE_NAME2/k22", HttpStatus.OK) == ''
        assert resOfGet("/admin/dev/cache/$CACHE_NAME2/k223", HttpStatus.OK) == ''
    }

}
