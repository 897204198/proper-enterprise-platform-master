//package com.proper.enterprise.platform.notice.server.push.controller
//
//import com.proper.enterprise.platform.sys.i18n.I18NUtil
//import org.junit.Test
//import org.springframework.http.HttpStatus
//import com.proper.enterprise.platform.core.entity.DataTrunk
//import com.proper.enterprise.platform.notice.server.push.vo.PushVO
//import com.proper.enterprise.platform.test.AbstractTest
//import com.proper.enterprise.platform.test.utils.JSONUtil
//
//class PushControllerTest extends AbstractTest {
//
//    private static final URL = "/push"
//
//    @Test
//    void "post"() {
//        mockUser('test1', 't1', 'pwd')
//        assert I18NUtil.getMessage("notice-server-push.test.max.error") == post(URL, JSONUtil.toJSON(new PushVO().setTest(999)), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString()
//        PushVO result = postAndReturn(URL, new PushVO())
//        expect:
//        assert null != result.getId()
//    }
//
//    @Test
//    void "delete"() {
//        mockUser('test1', 't1', 'pwd')
//        delete(URL + "?id=1", HttpStatus.NOT_FOUND)
//        PushVO result = postAndReturn(URL, new PushVO())
//        delete(URL + "?id=" + result.getId(), HttpStatus.NO_CONTENT)
//    }
//
//    @Test
//    void "put"() {
//        mockUser('test1', 't1', 'pwd')
//        PushVO vo = new PushVO()
//        vo.setTest(1)
//        PushVO result = postAndReturn(URL, vo)
//        result.setTest(2)
//        PushVO result2 = JSONUtil.parse(put(URL, JSONUtil.toJSON(result), HttpStatus.OK)
//            .getResponse().getContentAsString(), PushVO.class)
//        expect:
//        assert 2 == result2.getTest()
//    }
//
//    @Test
//    void "get"() {
//        mockUser('test1', 't1', 'pwd')
//        PushVO vo1 = new PushVO().setTest(1)
//        PushVO vo2 = new PushVO().setTest(2)
//        PushVO vo3 = new PushVO().setTest(3)
//        postAndReturn(URL, vo1)
//        postAndReturn(URL, vo2)
//        postAndReturn(URL, vo3)
//        List<PushVO> list = JSONUtil.parse(get(URL, HttpStatus.OK).getResponse().getContentAsString(), List)
//        DataTrunk<PushVO> page = JSONUtil.parse(get(URL + "?pageNo=1&pageSize=1", HttpStatus.OK).getResponse().getContentAsString(), DataTrunk)
//        List<PushVO> queryTest3 = JSONUtil.parse(get(URL + "?test=3", HttpStatus.OK).getResponse().getContentAsString(), List)
//
//        expect:
//        assert 3 == list.size()
//        assert 3 == page.count
//        assert 1 == page.getData().size()
//        assert 1 == queryTest3.size()
//        assert 3 == queryTest3.get(0).test
//    }
//}
