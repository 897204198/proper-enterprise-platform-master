package com.proper.enterprise.platform.sys.datadic.controller

import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.core.i18n.I18NUtil
import com.proper.enterprise.platform.sys.datadic.entity.DataDicEntity
import com.proper.enterprise.platform.sys.datadic.enums.DataDicTypeEnum
import com.proper.enterprise.platform.sys.datadic.service.DataDicCatalogService
import com.proper.enterprise.platform.sys.datadic.service.DataDicService
import com.proper.enterprise.platform.sys.datadic.vo.DataDicCatalogVO
import com.proper.enterprise.platform.test.AbstractJPATest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class DataDicCatalogControllerTest extends AbstractJPATest {

    def datadicUrl = "/sys/datadic/catalog"

    @Autowired
    private DataDicCatalogService dataDicCatalogService

    @Autowired
    private DataDicService dataDicService

    @Test
    void testSave() {
        DataDicCatalogVO dataDicCatalogVO = new DataDicCatalogVO()
        post(datadicUrl, JSONUtil.toJSON(dataDicCatalogVO),
            HttpStatus.INTERNAL_SERVER_ERROR)
            .getResponse()
            .getContentAsString() == I18NUtil.getMessage("pep.sys.datadic.catalog.empty")
        dataDicCatalogVO.setCatalogCode("catalogCode")
        post(datadicUrl, JSONUtil.toJSON(dataDicCatalogVO),
            HttpStatus.INTERNAL_SERVER_ERROR)
            .getResponse()
            .getContentAsString() == I18NUtil.getMessage("pep.sys.datadic.catalog.name.empty")
        dataDicCatalogVO.setCatalogName("catalogName")
        DataDicCatalogVO saveDicCatalog = resOfPost(datadicUrl, dataDicCatalogVO)
        assert saveDicCatalog.getEnable()
        assert saveDicCatalog.getCatalogType() == DataDicTypeEnum.SYSTEM

        DataDicCatalogVO dataDicCatalogVO2 = new DataDicCatalogVO()
        dataDicCatalogVO2.setCatalogCode("catalogCode")
        dataDicCatalogVO2.setCatalogName("code")
        post(datadicUrl, JSONUtil.toJSON(dataDicCatalogVO2),
            HttpStatus.INTERNAL_SERVER_ERROR)
            .getResponse()
            .getContentAsString() == I18NUtil.getMessage("pep.sys.datadic.catalog.code.unique")
        dataDicCatalogVO2.setCatalogCode("catalogCode1")
        resOfPost(datadicUrl, dataDicCatalogVO2)
    }

    @Test
    void testUpdate() {
        DataDicCatalogVO dataDicCatalogVO = new DataDicCatalogVO()
        dataDicCatalogVO.setCatalogCode("catalog")
        dataDicCatalogVO.setCatalogName("code")
        DataDicCatalogVO saveDataDicCatalogVO = resOfPost(datadicUrl, dataDicCatalogVO)

        DataDicEntity dataDic1 = new DataDicEntity()
        dataDic1.setCatalog("catalog")
        dataDic1.setCode("code")
        dataDic1.setName("name")
        dataDic1.setOrder(1)
        DataDicEntity saveDic = resOfPost("/sys/datadic", dataDic1)
        DataDicEntity queryDataDic = JSONUtil.parse(get("/sys/datadic"
            + "/catalog/catalog/code/code", HttpStatus.OK).getResponse().getContentAsString(), DataDicEntity.class)
        assert saveDic.getId() == queryDataDic.getId()

        DataDicCatalogVO queryDataDicCatalogVO = JSONUtil.parse(get(datadicUrl + "/id/" + saveDataDicCatalogVO.getId(), HttpStatus.OK)
            .getResponse()
            .getContentAsString(),
            DataDicCatalogVO.class)
        assert saveDataDicCatalogVO.getId() == queryDataDicCatalogVO.getId()
        saveDataDicCatalogVO.setCatalogCode("tt")
        saveDataDicCatalogVO.setCatalogName("name2")
        DataDicCatalogVO updateDataDicCatalogVO = resOfPut("$datadicUrl/${saveDataDicCatalogVO.getId()}", JSONUtil.toJSON(saveDataDicCatalogVO), HttpStatus.OK)
        DataDicCatalogVO queryUpdateDataDicCatalogVO = JSONUtil.parse(get(datadicUrl + "/id/" + saveDataDicCatalogVO.getId(), HttpStatus.OK)
            .getResponse()
            .getContentAsString(),
            DataDicCatalogVO.class)
        assert queryUpdateDataDicCatalogVO.getCatalogName() == updateDataDicCatalogVO.getCatalogName()

        DataDicEntity queryDataDic2 = JSONUtil.parse(get("/sys/datadic"
            + "/catalog/tt/code/code", HttpStatus.OK).getResponse().getContentAsString(), DataDicEntity.class)
        assert saveDic.getId() == queryDataDic.getId()
        assert queryDataDic2.getCatalog() == "tt"
    }

    @Test
    void testDelete() {
        DataDicCatalogVO dataDicCatalogVO = new DataDicCatalogVO()
        dataDicCatalogVO.setCatalogCode("catalog")
        dataDicCatalogVO.setCatalogName("code")
        DataDicCatalogVO saveDataDicCatalogVO = resOfPost(datadicUrl, dataDicCatalogVO)
        DataDicEntity dataDic1 = new DataDicEntity()
        dataDic1.setCatalog("catalog")
        dataDic1.setCode("code")
        dataDic1.setName("name")
        dataDic1.setOrder(1)
        DataDicEntity saveDic = resOfPost("/sys/datadic", dataDic1)
        DataDicCatalogVO queryDataDic = JSONUtil.parse(get(datadicUrl + "/id/" + saveDataDicCatalogVO.getId(), HttpStatus.OK).getResponse().getContentAsString(), DataDicCatalogVO.class)
        assert saveDataDicCatalogVO.getId() == queryDataDic.getId()
        delete(datadicUrl + "?ids=" + saveDataDicCatalogVO.getId(), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getErrorMessage() == I18NUtil.getMessage("pep.sys.datadic.catalog.del.relevance.error")

        delete("/sys/datadic" + "?ids=" + saveDic.getId(), HttpStatus.NO_CONTENT)
        delete(datadicUrl + "?ids=" + saveDataDicCatalogVO.getId(), HttpStatus.NO_CONTENT)
        assert "" == get(datadicUrl + "/id/" + saveDataDicCatalogVO.getId(), HttpStatus.OK).getResponse().getContentAsString()
    }

    @Test
    void testQuery() {
        DataDicCatalogVO dataDicCatalogVO = new DataDicCatalogVO()
        dataDicCatalogVO.setCatalogCode("catalog")
        dataDicCatalogVO.setCatalogName("name")
        dataDicCatalogVO.setSort(5)
        DataDicCatalogVO dataDicCatalogVO2 = new DataDicCatalogVO()
        dataDicCatalogVO2.setCatalogCode("catalog2")
        dataDicCatalogVO2.setCatalogName("name2")
        dataDicCatalogVO.setSort(3)
        DataDicCatalogVO sava1 = dataDicCatalogService.save(dataDicCatalogVO)
        DataDicCatalogVO sava2 = dataDicCatalogService.save(dataDicCatalogVO2)

        DataTrunk<DataDicCatalogVO> dataDicEntities = JSONUtil.parse(get(datadicUrl + "?catalogType=SYSTEM", HttpStatus.OK).getResponse()
            .getContentAsString(), DataTrunk.class)
        assert dataDicEntities.data.size() == 2
        DataTrunk<DataDicCatalogVO> dataDicEntities2 = JSONUtil.parse(get(datadicUrl + "?catalogType=BUSINESS", HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert dataDicEntities2.data.size() == 0
        sava2.setEnable(false)
        dataDicCatalogService.update(sava2)

        DataTrunk<DataDicCatalogVO> dataDicCatalogsDis = JSONUtil.parse(get(datadicUrl + "?enable=DISABLE", HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert dataDicCatalogsDis.data.size() == 1

        DataTrunk<DataDicCatalogVO> dataDicCatalogsALL = JSONUtil.parse(get(datadicUrl + "?catalogCode=catalog&enable=ALL", HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert dataDicCatalogsALL.data.size() == 1

        DataTrunk<DataDicCatalogVO> dataDicCatalogsSort = JSONUtil.parse(get(datadicUrl, HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert dataDicCatalogsSort.data.size() == 2
        assert dataDicCatalogsSort.data.get(0).catalogName == 'name2'
        assert dataDicCatalogsSort.data.get(1).catalogName == 'name'
    }


    @Test
    void testParent() {
        DataDicCatalogVO dataDicCatalogVO = new DataDicCatalogVO()
        dataDicCatalogVO.setCatalogCode("catalog")
        dataDicCatalogVO.setCatalogName("code")
        DataDicCatalogVO saveDataDicCatalogVO = resOfPost(datadicUrl, dataDicCatalogVO)

        DataDicCatalogVO childRenVO = new DataDicCatalogVO()
        childRenVO.setCatalogCode("children")
        childRenVO.setCatalogName("children")
        childRenVO.setParentId(saveDataDicCatalogVO.getId())
        DataDicCatalogVO saveChildrenVO = resOfPost(datadicUrl, childRenVO)

        DataTrunk<DataDicCatalogVO> dataDicCatalogsSort = JSONUtil.parse(get(datadicUrl + '?catalogCode=children', HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert dataDicCatalogsSort.data.size() == 1
        assert dataDicCatalogsSort.data.get(0).catalogName == 'children'
        assert dataDicCatalogsSort.data.get(0).parentId == saveDataDicCatalogVO.getId()

        delete(datadicUrl + "?ids=" + saveDataDicCatalogVO.getId(), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getErrorMessage() == I18NUtil.getMessage("pep.sys.datadic.catalog.del.relevanceChildren.error")
    }

    @Test
    void testQueryByParent() {
        DataDicCatalogVO dataDicCatalogVO = new DataDicCatalogVO()
        dataDicCatalogVO.setCatalogCode("catalog")
        dataDicCatalogVO.setCatalogName("code")
        DataDicCatalogVO saveDataDicCatalogVO = resOfPost(datadicUrl, dataDicCatalogVO)

        DataDicCatalogVO childRenVO = new DataDicCatalogVO()
        childRenVO.setCatalogCode("children")
        childRenVO.setCatalogName("children")
        childRenVO.setParentId(saveDataDicCatalogVO.getId())
        DataDicCatalogVO saveChildrenVO = resOfPost(datadicUrl, childRenVO)
        DataDicCatalogVO childRenChildRenVO = new DataDicCatalogVO()
        childRenChildRenVO.setCatalogCode("childrenChildren")
        childRenChildRenVO.setCatalogName("childrenChildren")
        childRenChildRenVO.setParentId(saveChildrenVO.getId())
        DataDicCatalogVO saveChildrenChildrenVO = resOfPost(datadicUrl, childRenChildRenVO)

        DataDicEntity dataDic1 = new DataDicEntity()
        dataDic1.setCatalog("children")
        dataDic1.setCode("code")
        dataDic1.setName("name")
        dataDic1.setDeft(true)
        dataDic1.setOrder(1)
        DataDicEntity dataDic2 = new DataDicEntity()
        dataDic2.setCatalog("children")
        dataDic2.setCode("code2")
        dataDic2.setName("name2")
        dataDic2.setOrder(2)
        DataDicEntity dataDic3 = new DataDicEntity()
        dataDic3.setCatalog("childrenChildren")
        dataDic3.setCode("code3")
        dataDic3.setName("name3")
        dataDic3.setOrder(1)
        DataDicEntity sava1 = dataDicService.save(dataDic1)
        DataDicEntity sava2 = dataDicService.save(dataDic2)
        DataDicEntity sava3 = dataDicService.save(dataDic3)

        List<DataDicCatalogVO> dataDicCatalogs = JSONUtil.parse(get(datadicUrl + "/parentCatalog/catalog", HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert dataDicCatalogs.size() == 3
        assert dataDicCatalogs.get(0)['id'] == saveDataDicCatalogVO.getId()
        assert dataDicCatalogs.get(1)['parentId'] == saveDataDicCatalogVO.getId()
        assert dataDicCatalogs.get(1)['dataDics'].size == 2
        assert dataDicCatalogs.get(2)['parentId'] == saveChildrenVO.getId()
        assert dataDicCatalogs.get(2)['dataDics'].size == 1
    }
}
