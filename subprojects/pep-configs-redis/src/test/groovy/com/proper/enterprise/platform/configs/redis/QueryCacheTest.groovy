package com.proper.enterprise.platform.configs.redis
import com.proper.enterprise.platform.configs.redis.entity.AEntity
import com.proper.enterprise.platform.configs.redis.repository.ARepository
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager

class QueryCacheTest extends AbstractTest {

    @Autowired
    CacheManager cacheManager

    @Autowired
    ARepository repo

    @Before
    public void setUp() {
        repo.save(new AEntity('abc', '123'))
    }

    @Test
    public void checkQueryCache() {
        println " ===== Should print insert and select ===== "
        AEntity entity = repo.findByUsername('abc')
        println " ===== print insert and select done ======= "

        println " ===== Use secondary cache to query ===== "
        3.times {
            repo.findByUsername('abc')
        }
        println " ===== cache query end ================== "

        println " ===== Should update entity and need to select to update cache ===== "
        entity.setDescription('update_account')
        repo.save(entity)
        repo.findByUsername('abc')
        println " ===== then should use cache again ===== "
        3.times {
            repo.findByUsername('abc')
        }

        println " ===== Method without @CacheQuery will do select each time ===== "
        3.times {
            repo.findByUsernameLike('abc')
        }
        println " ===== THE END ===== "

    }

}
