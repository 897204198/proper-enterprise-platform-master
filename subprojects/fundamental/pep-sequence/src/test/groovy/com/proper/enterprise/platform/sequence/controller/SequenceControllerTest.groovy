package com.proper.enterprise.platform.sequence.controller

import com.proper.enterprise.platform.core.utils.DateUtil

import com.proper.enterprise.platform.sequence.util.SerialNumberUtil
import com.proper.enterprise.platform.core.i18n.I18NUtil
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager
import org.springframework.http.HttpStatus
import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.sequence.vo.SequenceVO
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.springframework.test.context.jdbc.Sql

import java.time.LocalDateTime

@Sql(["/com/proper/enterprise/platform/sequence/sequenceConfigData.sql"])
class SequenceControllerTest extends AbstractJPATest {

    private static final URL = "/sequence"

    @Autowired
    private CacheManager cacheManager

    @Test
    void "post"() {
        mockUser('test1', 't1', 'pwd')
        def sequenceVOTest = [:]
        sequenceVOTest['sequenceCode'] = ''
        sequenceVOTest['sequenceName'] = 'test'
        sequenceVOTest['formula'] = '${length:8}'
        sequenceVOTest['clearType'] = ['catalog':'CLEAR_TYPE', 'code':'NO_CLEAR']
        assert I18NUtil.getMessage("sequence.sequenceCode.notblank.error") == post(URL, JSONUtil.toJSON(sequenceVOTest), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString()
        if (cacheManager.getCache("pep.cache.ehCacheSerialNumberHandler") != null) {
            cacheManager.getCache("pep.cache.ehCacheSerialNumberHandler").clear()
        }
        if (cacheManager.getCache("test") != null) {
            cacheManager.getCache("test").clear()
        }
        def sequenceVO = [:]
        sequenceVO['sequenceCode'] = 'test'
        sequenceVO['sequenceName'] = 'test'
        sequenceVO['formula'] = 'HT${date:yyyy}proper${length:8}'
        sequenceVO['clearType'] = ['catalog':'CLEAR_TYPE', 'code':'NO_CLEAR']
        SequenceVO result = JSONUtil.parse(post(URL, JSONUtil.toJSON(sequenceVO), HttpStatus.CREATED).getResponse().getContentAsString(), SequenceVO.class)
        assert null != result.getId()
        assert '00000000' == result.getInitialValue()
        assert '00000000' == SerialNumberUtil.getCurrentSerialNumber("test")
        assert 'HT' + DateUtil.toString(LocalDateTime.now(), "yyyy") + 'proper00000001' == SerialNumberUtil.generate("test")
    }

    @Test
    void "delete"() {
        mockUser('test1', 't1', 'pwd')
        delete(URL + "?ids=1", HttpStatus.NOT_FOUND)
        delete(URL + "?ids=001", HttpStatus.NO_CONTENT)
    }

    @Test
    void "put"() {
        mockUser('test1', 't1', 'pwd')
        testSerial()
        def sequenceVO = [:]
        sequenceVO['sequenceCode'] = 'generatetest2'
        sequenceVO['sequenceName'] = '测试2'
        sequenceVO['formula'] = '${length:8}'
        sequenceVO['clearType'] = ['catalog':'CLEAR_TYPE', 'code':'NO_CLEAR']
        sequenceVO['initialValue'] = '00000000'
        SequenceVO result2 = JSONUtil.parse(put(URL + "/002", JSONUtil.toJSON(sequenceVO), HttpStatus.OK)
            .getResponse().getContentAsString(), SequenceVO.class)
        assert '${length:8}' == result2.getFormula()
        assert '00000000' == result2.getInitialValue()
        assert '00000001' == SerialNumberUtil.generate("generatetest2")
    }

    @Test
    void "get"() {
        mockUser('test1', 't1', 'pwd')
        DataTrunk<SequenceVO> list = JSONUtil.parse(get(URL, HttpStatus.OK).getResponse().getContentAsString(), DataTrunk)
        DataTrunk<SequenceVO> page = JSONUtil.parse(get(URL + "?pageNo=1&pageSize=1", HttpStatus.OK).getResponse().getContentAsString(), DataTrunk)
        DataTrunk<SequenceVO> queryTest3 = JSONUtil.parse(get(URL + "?sequenceCode=generatetest2", HttpStatus.OK).getResponse().getContentAsString(), DataTrunk)

        assert 2 == list.count
        assert 2 == page.count
        assert 1 == page.getData().size()
        assert 1 == queryTest3.count
        assert 'generatetest2' == queryTest3.getData().sequenceCode.get(0)
    }

    void testSerial() {
        if (cacheManager.getCache("pep.cache.serialNumberHandler") != null) {
            cacheManager.getCache("pep.cache.serialNumberHandler").clear()
        }
        assert "HT" + DateUtil.toString(LocalDateTime.now(), "yyyyMM") + "001" == SerialNumberUtil.generate("generatetest1")
        assert "HT" + DateUtil.toString(LocalDateTime.now(), "yyyyMM") + "002" == SerialNumberUtil.generate("generatetest1")
        assert "00001" == SerialNumberUtil.generate("generatetest2")
        assert "00002" == SerialNumberUtil.generate("generatetest2")
    }
}
