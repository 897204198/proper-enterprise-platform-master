package com.proper.enterprise.platform.cache.redis

import com.proper.enterprise.platform.cache.redis.entity.AEntity
import com.proper.enterprise.platform.cache.redis.repository.ARepository
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.annotation.NoTx
import org.hibernate.Session
import org.hibernate.stat.Statistics
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import javax.persistence.EntityManager

class QueryCacheTest extends AbstractTest {

    @Autowired
    private ARepository repo

    @Autowired
    private EntityManager entityManager

    @Test
    @NoTx
    void checkQueryCache() {
        Session session = (Session) entityManager.getDelegate()
        Statistics statistics = session.getSessionFactory().getStatistics()
        statistics.setStatisticsEnabled(true)

        repo.save(new AEntity('abc', '123'))
        AEntity entity = repo.findByUsername('abc')
        println " ===== print insert and select done ======= "
        // cache entity after first load
//        assert statistics.queryCachePutCount == 1
//        assert statistics.updateTimestampsCachePutCount == 1
        showCounts(statistics)

        println " ===== Use secondary cache to query ===== "
        // hit count of cache will be increased after each load operation
        3.times {
            repo.findByUsername('abc')
            showCounts(statistics)
        }
        println " ===== cache query end ================== "

        println " ===== Should update entity and need to select to update cache ===== "
        entity.setDescription('update_account')
        repo.save(entity)

        repo.findByUsername('abc')

        showCounts(statistics)

        println " ===== then should use cache again ===== "
        3.times {
            repo.findByUsername('abc')
            showCounts(statistics)
        }

        println " ===== Method without @CacheQuery will do select each time ===== "
        3.times {
            repo.findByUsernameLike('abc')
            showCounts(statistics)
        }
        println " ===== THE END ===== "
    }

    def showCounts(Statistics statistics) {
        println "Query Cache Put: ${statistics.getQueryCachePutCount()}"
        println "Query Cache Hit: ${statistics.getQueryCacheHitCount()}"
        println "Query Cache Miss: ${statistics.getQueryCacheMissCount()}"

        println "Update Timestamps Cache Put: ${statistics.getUpdateTimestampsCachePutCount()}"
        println "Update Timestamps Cache Hit: ${statistics.getUpdateTimestampsCacheHitCount()}"
        println "Update Timestamps Cache Miss: ${statistics.getUpdateTimestampsCacheMissCount()}"
    }

}
