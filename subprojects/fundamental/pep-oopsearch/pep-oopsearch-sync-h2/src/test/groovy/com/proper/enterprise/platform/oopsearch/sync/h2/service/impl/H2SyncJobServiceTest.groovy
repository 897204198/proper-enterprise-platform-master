package com.proper.enterprise.platform.oopsearch.sync.h2.service.impl

import com.proper.enterprise.platform.oopsearch.config.repository.SearchConfigRepository
import com.proper.enterprise.platform.oopsearch.repository.SyncMongoRepository
import com.proper.enterprise.platform.oopsearch.sync.h2.entity.DemoDeptEntity
import com.proper.enterprise.platform.oopsearch.sync.h2.entity.DemoTestEntity
import com.proper.enterprise.platform.oopsearch.sync.h2.repository.DemoDeptRepository
import com.proper.enterprise.platform.oopsearch.sync.h2.repository.DemoTestRepository
import com.proper.enterprise.platform.test.AbstractJPATest
import com.proper.enterprise.platform.test.annotation.NoTx
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.test.context.jdbc.Sql


class H2SyncJobServiceTest extends AbstractJPATest{

    @Autowired
    H2SyncJobService h2SyncJobService

    @Autowired
    private DemoDeptRepository demoDeptRepository

    @Autowired
    private DemoTestRepository demoTestRepository

    @Autowired
    private SyncMongoRepository syncMongoRepository

    @Autowired
    SearchConfigRepository searchConfigRepository

    @Autowired
    CacheManager cacheManager

    @Ignore
    @NoTx
    @Test
    @Sql([
            "/sql/oopsearch/sync/h2/demoTestConfigData.sql",
            "/sql/oopsearch/sync/h2/demoDeptConfigData.sql"
    ])
    void testFullSyncMongo(){
        // 清除cachequery的缓存对象，避免通过@sql插入db的数据因为没有触发cache进行更新，而导致查询时出现脏读现象
        Cache queryCache = cacheManager.getCache("org.hibernate.cache.internal.StandardQueryCache")
        queryCache.clear()

        initDemoDeptData()
        initDemoTestData()

//        sleep(5000)//等待定时任务自动同步数据到mongo
//        h2SyncJobService.fullSyncMongo()
        println "------开始查询mongo-----"
        int count = 0
        println "------查询mongo结束，count："+count+"-----"
        // 5 cols * 3 rows - 1 duplicated value + 3 cols from DemoTest
        for(i in 1..20){
            count = syncMongoRepository.findAll().size()
            if (count == 5 * 3 - 1 + 3) {
                break
            } else {
                sleep(1000)
            }
        }
        assert count == 5 * 3 - 1 + 3
    }

    @Before
    void initMongo(){
        syncMongoRepository.deleteAll()
    }


    void initDemoDeptData(){
        DemoDeptEntity searchEntity = new DemoDeptEntity()
        searchEntity.setDeptId("001")
        searchEntity.setDeptName("研发部")
        searchEntity.setDeptDesc("产品研发")
        searchEntity.setCreateTime("2018-01-01")
        searchEntity.setDeptMemberCount(10)

        demoDeptRepository.save(searchEntity)

        DemoDeptEntity searchEntity2 = new DemoDeptEntity()
        searchEntity2.setDeptId("002")
        searchEntity2.setDeptName("实施部")
        // 与dept_id字段内容重复，但分属不同字段，仍然会插入mongodb中
        searchEntity2.setDeptDesc("001")
        searchEntity2.setCreateTime("2018-01-02")
        searchEntity2.setDeptMemberCount(20)

        demoDeptRepository.save(searchEntity2)

        DemoDeptEntity searchEntity3 = new DemoDeptEntity()
        // 与dept_id字段内容重复，且字段相同，不插入mongodb
        searchEntity3.setDeptId("001")
        searchEntity3.setDeptName("销售部")
        searchEntity3.setDeptDesc("产品销售")
        searchEntity3.setCreateTime("2018-01-03")
        searchEntity3.setDeptMemberCount(30)

        demoDeptRepository.save(searchEntity3)

    }

    void initDemoTestData(){
        // 复合主键的entity
        DemoTestEntity demoTestEntity = new DemoTestEntity()
        demoTestEntity.setPri2("pri2")
        demoTestEntity.setName("test")

        demoTestRepository.save(demoTestEntity)
    }
}
