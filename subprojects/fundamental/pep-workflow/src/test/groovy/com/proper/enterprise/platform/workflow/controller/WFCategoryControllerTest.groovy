package com.proper.enterprise.platform.workflow.controller

import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.core.i18n.I18NUtil
import com.proper.enterprise.platform.test.AbstractJPATest
import com.proper.enterprise.platform.test.utils.JSONUtil
import com.proper.enterprise.platform.workflow.repository.WFCategoryRepository
import com.proper.enterprise.platform.workflow.vo.WFCategoryVO
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

@Sql("/com/proper/enterprise/platform/workflow/wfCategory.sql")
class WFCategoryControllerTest extends AbstractJPATest {

    @Autowired
    WFCategoryRepository wfCategoryRepository

    private static final URL = "/repository/wfCategory"

    @Test
    void "post"() {
        mockUser('test1', 't1', 'pwd')
        WFCategoryVO wfCategoryVO = new WFCategoryVO()
        wfCategoryVO.setName("测试类别")
        wfCategoryVO.setCode("testCode")
        wfCategoryVO.setSort(1)
        wfCategoryVO.setParentId("parentCategory")

        WFCategoryVO result = resOfPost(URL, wfCategoryVO)
        expect:
        assert null != result.getId()
        assert "测试类别" == result.getName()
        assert "testCode" == result.getCode()
        assert "parentCategory" == result.getParentId()
        assert "PARENT_CATEGORY" == result.getParentCode()
        assert "父类别" == result.getParentName()

        wfCategoryVO = new WFCategoryVO()
        wfCategoryVO.setCode("testCode2")
        wfCategoryVO.setSort(2)
        wfCategoryVO.setParentId("parentCategory")
        assert I18NUtil.getMessage("workflow.category.name.notBlank") == post(URL, JSONUtil.toJSON(wfCategoryVO), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString()

        wfCategoryVO.setName("测试类别2")
        wfCategoryVO.setCode(null)
        assert I18NUtil.getMessage("workflow.category.code.notBlank") == post(URL, JSONUtil.toJSON(wfCategoryVO), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString()

        wfCategoryVO.setCode("testCode2")
        wfCategoryVO.setSort(null)
        assert I18NUtil.getMessage("workflow.category.sort.notBlank") == post(URL, JSONUtil.toJSON(wfCategoryVO), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString()

        wfCategoryVO.setName("测试类别")
        wfCategoryVO.setCode("testCode2")
        wfCategoryVO.setSort(1)
        wfCategoryVO.setParentId("parentCategory")
        assert I18NUtil.getMessage("workflow.category.name.unique") == post(URL, JSONUtil.toJSON(wfCategoryVO), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString()

        wfCategoryVO.setName("测试类别2")
        wfCategoryVO.setCode("testCode")
        wfCategoryVO.setSort(1)
        wfCategoryVO.setParentId("parentCategory")
        assert I18NUtil.getMessage("workflow.category.code.unique") == post(URL, JSONUtil.toJSON(wfCategoryVO), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString()
    }

    @Test
    void "delete"() {
        mockUser('test1', 't1', 'pwd')
        WFCategoryVO wfCategoryVO = new WFCategoryVO()
        wfCategoryVO.setName("测试类别")
        wfCategoryVO.setCode("testCode")
        wfCategoryVO.setSort(1)
        wfCategoryVO.setParentId("parentCategory")

        WFCategoryVO result = resOfPost(URL, wfCategoryVO)
        assert I18NUtil.getMessage("workflow.category.delete.relation.failed") == delete(URL + "?ids=parentCategory", HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString()

        delete(URL + "?ids=" + result.getId(), HttpStatus.NO_CONTENT)
        delete(URL + "?ids=parentCategory", HttpStatus.NO_CONTENT)
    }

    @Test
    void "put"() {
        mockUser('test1', 't1', 'pwd')
        WFCategoryVO wfCategoryVO = new WFCategoryVO()
        wfCategoryVO.setName("测试类别")
        wfCategoryVO.setCode("testCode")
        wfCategoryVO.setSort(1)
        wfCategoryVO.setParentId("parentCategory")

        WFCategoryVO result = resOfPost(URL, wfCategoryVO)
        result.setName("测试类别test")
        WFCategoryVO result2 = JSONUtil.parse(put(URL + "/" + result.getId(), JSONUtil.toJSON(result), HttpStatus.OK)
            .getResponse().getContentAsString(), WFCategoryVO.class)
        expect:
        assert "测试类别test" == result2.getName()
    }

    @Test
    void "get"() {
        mockUser('test1', 't1', 'pwd')
        WFCategoryVO vo1 = new WFCategoryVO()
        vo1.setName("测试类别")
        vo1.setCode("testCode")
        vo1.setSort(1)
        vo1.setParentId("parentCategory")
        vo1 = resOfPost(URL, vo1)
        WFCategoryVO vo2 = new WFCategoryVO()
        vo2.setName("测试类别2")
        vo2.setCode("testCode2")
        vo2.setSort(1)
        vo2.setParentId(vo1.getId())
        resOfPost(URL, vo2)
        WFCategoryVO vo3 = new WFCategoryVO()
        vo3.setName("测试类别3")
        vo3.setCode("testCode3")
        vo3.setSort(2)
        vo3.setParentId("parentCategory")
        resOfPost(URL, vo3)
        DataTrunk<WFCategoryVO> dataTrunk = JSONUtil.parse(get(URL, HttpStatus.OK).getResponse().getContentAsString(), DataTrunk)
        DataTrunk<WFCategoryVO> page = JSONUtil.parse(get(URL + "?pageNo=1&pageSize=1", HttpStatus.OK).getResponse().getContentAsString(), DataTrunk)
        DataTrunk<WFCategoryVO> queryTest2 = JSONUtil.parse(get(URL + "?parentId=parentCategory&pageNo=1&pageSize=10", HttpStatus.OK).getResponse().getContentAsString(), DataTrunk)
        DataTrunk<WFCategoryVO> queryTest3 = JSONUtil.parse(get(URL + "?name=测试类别2&code=testCode2", HttpStatus.OK).getResponse().getContentAsString(), DataTrunk)

        expect:
        assert 7 == dataTrunk.getCount()
        assert 7 == page.count
        assert 1 == page.getData().size()

        assert 2 == queryTest2.getCount()
        assert "测试类别" == queryTest2.getData()[0].name
        assert "测试类别3" == queryTest2.getData()[1].name

        assert 1 == queryTest3.getCount()
        assert "测试类别2" == queryTest3.getData()[0].name
    }
}
