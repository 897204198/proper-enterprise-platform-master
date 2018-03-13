package com.proper.enterprise.platform.oopsearch.service

import com.proper.enterprise.platform.oopsearch.api.document.OOPSearchDocument
import com.proper.enterprise.platform.oopsearch.api.serivce.SearchService
import com.proper.enterprise.platform.oopsearch.entity.Table2EntityTest
import com.proper.enterprise.platform.oopsearch.entity.TableEntityTest
import com.proper.enterprise.platform.oopsearch.configs.MultiTableConfigTest
import com.proper.enterprise.platform.oopsearch.repository.Table2RepositoryTest
import com.proper.enterprise.platform.oopsearch.repository.TableRepositoryTest
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

import java.text.SimpleDateFormat

class SearchServiceImplTest extends AbstractTest{

    @Autowired
    MultiTableConfigTest multiTableConfigTest

    @Autowired
    SearchService searchService

    @Autowired
    TableRepositoryTest tableRepositoryTest

    @Autowired
    Table2RepositoryTest table2RepositoryTest

    @Test
    void testSearchServiceImpl(){
        initData()
        get("/search/init",  HttpStatus.OK)
        List<OOPSearchDocument> resultList = searchService.getSearchInfo("张", multiTableConfigTest)
        assert resultList.size() == 3

        String date = new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime());
        resultList = searchService.getSearchInfo(date, multiTableConfigTest)
        //assert resultList.size() == 3 + 3 + 3 + 3

        date = new SimpleDateFormat("yyyy-mm").format(Calendar.getInstance().getTime());
        resultList = searchService.getSearchInfo(date, multiTableConfigTest)
        //assert resultList.size() == 3 + 3 + 3 + 2

        date = new SimpleDateFormat("yyyy-mm-dd").format(Calendar.getInstance().getTime());
        resultList = searchService.getSearchInfo(date, multiTableConfigTest)
        //assert resultList.size() == 3 + 3 + 2
    }

    void initData() {
        TableEntityTest tableEntityTest = new TableEntityTest()
        tableEntityTest.setUserId("001")
        tableEntityTest.setUserName("张一")
        tableEntityTest.setAge(30)
        tableEntityTest.setDeptId("001")
        tableEntityTest.setCreateTime("2018-01-01")

        TableEntityTest tableEntityTest2 = new TableEntityTest()
        tableEntityTest2.setUserId("002")
        tableEntityTest2.setUserName("张二")
        tableEntityTest2.setAge(32)
        tableEntityTest2.setDeptId("002")
        tableEntityTest2.setCreateTime("2018-01-02")

        TableEntityTest tableEntityTest3 = new TableEntityTest()
        tableEntityTest3.setUserId("003")
        tableEntityTest3.setUserName("张三")
        tableEntityTest3.setAge(33)
        tableEntityTest3.setDeptId("003")
        tableEntityTest3.setCreateTime("2018-02-03")

        tableRepositoryTest.save(tableEntityTest)
        tableRepositoryTest.save(tableEntityTest2)
        tableRepositoryTest.save(tableEntityTest3)

        Table2EntityTest table2EntityTest = new Table2EntityTest()
        table2EntityTest.setDeptId("001")
        table2EntityTest.setDeptName("研发部")
        table2EntityTest.setDeptDesc("产品研发")
        table2EntityTest.setCreateTime("2018-01-01")
        table2EntityTest.setDeptMemberCount(10)

        Table2EntityTest table2EntityTest2 = new Table2EntityTest()
        table2EntityTest2.setDeptId("002")
        table2EntityTest2.setDeptName("实施部")
        table2EntityTest2.setDeptDesc("产品实施")
        table2EntityTest2.setCreateTime("2018-01-02")
        table2EntityTest2.setDeptMemberCount(20)

        Table2EntityTest table2EntityTest3 = new Table2EntityTest()
        table2EntityTest3.setDeptId("003")
        table2EntityTest3.setDeptName("销售部")
        table2EntityTest3.setDeptDesc("产品销售")
        table2EntityTest3.setCreateTime("2018-01-03")
        table2EntityTest3.setDeptMemberCount(30)

        table2RepositoryTest.save(table2EntityTest)
        table2RepositoryTest.save(table2EntityTest2)
        table2RepositoryTest.save(table2EntityTest3)
    }
}
