package com.proper.enterprise.platform.sys.datadic.controller

import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.core.i18n.I18NUtil
import com.proper.enterprise.platform.sys.datadic.entity.DataDicEntity
import com.proper.enterprise.platform.sys.datadic.enums.DataDicTypeEnum
import com.proper.enterprise.platform.sys.datadic.service.DataDicService
import com.proper.enterprise.platform.test.AbstractJPATest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class DataDicControllerTest extends AbstractJPATest {

    def datadicUrl = "/sys/datadic"

    @Autowired
    private DataDicService dataDicService

    @Test
    void testSave() {
        DataDicEntity dataDic1 = new DataDicEntity()
        dataDic1.setDeft(true)
        post(datadicUrl, JSONUtil.toJSON(dataDic1), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString() == I18NUtil.getMessage("pep.sys.datadic.catalog.empty")
        dataDic1.setCatalog("catalog")
        post(datadicUrl, JSONUtil.toJSON(dataDic1), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString() == I18NUtil.getMessage("pep.sys.datadic.code.empty")
        dataDic1.setCode("code")
        post(datadicUrl, JSONUtil.toJSON(dataDic1), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString() == I18NUtil.getMessage("pep.sys.datadic.name.empty")
        dataDic1.setName("name")
        dataDic1.setOrder(1)
        DataDicEntity saveDic = resOfPost(datadicUrl, JSONUtil.toJSON(dataDic1), HttpStatus.CREATED)
        assert saveDic.getEnable()
        assert saveDic.getDataDicType() == DataDicTypeEnum.SYSTEM

        DataDicEntity dataDic2 = new DataDicEntity()
        dataDic2.setCatalog("catalog")
        dataDic2.setCode("code")
        dataDic2.setName("name")
        post(datadicUrl, JSONUtil.toJSON(dataDic2), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString() == I18NUtil.getMessage("pep.sys.datadic.unique")
        dataDic2.setCatalog("catalog")
        dataDic2.setCode("code1")
        dataDic2.setName("name")
        dataDic2.setDeft(true)
        dataDic2.setOrder(2)
        post(datadicUrl, JSONUtil.toJSON(dataDic2), HttpStatus.CREATED)
    }

    @Test
    void testUpdate() {
        DataDicEntity dataDic1 = new DataDicEntity()
        dataDic1.setCatalog("catalog")
        dataDic1.setCode("code")
        dataDic1.setName("name")
        dataDic1.setOrder(1)
        DataDicEntity saveDic = resOfPost(datadicUrl, JSONUtil.toJSON(dataDic1), HttpStatus.CREATED)
        DataDicEntity queryDataDic = JSONUtil.parse(get(datadicUrl + "/catalog/catalog/code/code", HttpStatus.OK).getResponse().getContentAsString(), DataDicEntity.class)
        assert saveDic.getId() == queryDataDic.getId()
        saveDic.setName("name2")
        DataDicEntity updateDic = resOfPut("$datadicUrl/${saveDic.getId()}", JSONUtil.toJSON(saveDic), HttpStatus.OK)
        DataDicEntity queryUpdateDataDic = JSONUtil.parse(get(datadicUrl + "/catalog/catalog/code/code", HttpStatus.OK).getResponse().getContentAsString(), DataDicEntity.class)
        assert get(datadicUrl + "/catalog/catalog/code/code", HttpStatus.OK).getResponse().getContentAsString().contains("deft")
        assert updateDic.getName() == queryUpdateDataDic.getName()
    }

    @Test
    void testDelete() {
        DataDicEntity dataDic1 = new DataDicEntity()
        dataDic1.setCatalog("catalog")
        dataDic1.setCode("code")
        dataDic1.setName("name")
        dataDic1.setOrder(1)
        DataDicEntity saveDic = resOfPost(datadicUrl, JSONUtil.toJSON(dataDic1), HttpStatus.CREATED)
        DataDicEntity queryDataDic = JSONUtil.parse(get(datadicUrl + "/" + saveDic.getId(), HttpStatus.OK).getResponse().getContentAsString(), DataDicEntity.class)
        assert saveDic.getId() == queryDataDic.getId()
        delete(datadicUrl + "?ids=" + saveDic.getId(), HttpStatus.NO_CONTENT)
        assert "" == get(datadicUrl + "/" + saveDic.getId(), HttpStatus.OK).getResponse().getContentAsString()
    }


    @Test
    void testQuery() {
        DataDicEntity dataDic1 = new DataDicEntity()
        dataDic1.setCatalog("catalog")
        dataDic1.setCode("code")
        dataDic1.setName("name")
        dataDic1.setDeft(true)
        dataDic1.setOrder(1)
        DataDicEntity dataDic2 = new DataDicEntity()
        dataDic2.setCatalog("catalog")
        dataDic2.setCode("code2")
        dataDic2.setName("name2")
        dataDic2.setOrder(2)
        DataDicEntity sava1 = dataDicService.save(dataDic1)
        DataDicEntity sava2 = dataDicService.save(dataDic2)
        DataTrunk<DataDicEntity> dataDicEntities = JSONUtil.parse(get(datadicUrl + "?dataDicType=SYSTEM", HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert dataDicEntities.count == 2
        DataTrunk<DataDicEntity> dataDicEntities2 = JSONUtil.parse(get(datadicUrl + "?dataDicType=BUSINESS", HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert dataDicEntities2.count == 0
        sava2.setEnable(false)
        dataDicService.updateForSelective(sava2)
        List<DataDicEntity> dataDicCatalogs = JSONUtil.parse(get(datadicUrl + "/catalog/catalog", HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert dataDicCatalogs.size() == 1

        List<DataDicEntity> dataDicCatalogsDis = JSONUtil.parse(get(datadicUrl + "/catalog/catalog?enable=DISABLE", HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert dataDicCatalogsDis.size() == 1

        List<DataDicEntity> dataDicCatalogsALL = JSONUtil.parse(get(datadicUrl + "/catalog/catalog?enable=ALL", HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert dataDicCatalogsALL.size() == 2
    }
}
