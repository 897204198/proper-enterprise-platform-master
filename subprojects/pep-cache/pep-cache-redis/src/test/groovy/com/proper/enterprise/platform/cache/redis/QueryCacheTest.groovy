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

    private Statistics statistics

    @Test
    @NoTx
    void checkQueryCache() {
        Session session = (Session) entityManager.getDelegate()
        statistics = session.getSessionFactory().getStatistics()
        statistics.setStatisticsEnabled(true)

        repo.save(new AEntity('abc', '123'))
        AEntity entity = repo.findByUsername('abc')
        // cache entity after first load
        assertPut 1
        assertMiss 1

        // hit count of cache will be increased after each load operation
        3.times { idx ->
            repo.findByUsername('abc')
            assertHit idx+1
        }

        entity.setDescription('update_account')
        repo.save(entity)
        repo.findByUsername('abc')
        assertPut 2
        assertHit 3
        assertMiss 2

        3.times { idx ->
            repo.findByUsername('abc')
            assertHit idx+3+1
        }

        // Method without @CacheQuery will do select each time and not in statistics
        3.times { idx ->
            repo.findByUsernameLike('abc')
            assertPut 2
            assertHit 6
            assertMiss 2
        }
    }

    def assertPut(put) {
        assert statistics.getQueryCachePutCount() == put
    }

    def assertHit(hit) {
        assert statistics.getQueryCacheHitCount() == hit
    }

    def assertMiss(miss) {
        assert statistics.getQueryCacheMissCount() == miss
    }

}
