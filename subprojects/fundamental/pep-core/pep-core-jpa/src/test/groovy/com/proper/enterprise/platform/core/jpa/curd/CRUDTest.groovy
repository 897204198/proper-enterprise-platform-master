package com.proper.enterprise.platform.core.jpa.curd

import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.core.jpa.curd.a.repository.ARepository
import com.proper.enterprise.platform.core.jpa.curd.a.vo.AVO
import com.proper.enterprise.platform.core.jpa.curd.b.entity.BEntity
import com.proper.enterprise.platform.core.jpa.curd.b.repository.BRepository
import com.proper.enterprise.platform.core.jpa.curd.b.vo.BVO
import com.proper.enterprise.platform.core.jpa.curd.c.repository.CRepository
import com.proper.enterprise.platform.core.jpa.curd.c.vo.CVO
import com.proper.enterprise.platform.test.AbstractJPATest
import com.proper.enterprise.platform.test.annotation.NoTx
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class CRUDTest extends AbstractJPATest {

    private static final URL = "/a"

    @Autowired
    private BRepository brepository

    @Autowired
    private ARepository arepository

    @Autowired
    private CRepository crepository

    @Test
    void "post"() {
        mockUser('test1', 't1', 'pwd')
        AVO result = resOfPost(URL, JSONUtil.toJSON(new AVO()), HttpStatus.CREATED)
        expect:
        assert null != result.getId()
    }

    @Test
    void "delete"() {
        mockUser('test1', 't1', 'pwd')
        delete(URL + "?id=1", HttpStatus.NOT_FOUND)
        AVO avo = resOfPost(URL, JSONUtil.toJSON(new AVO()), HttpStatus.CREATED)
        delete(URL + "?id=" + avo.getId(), HttpStatus.NO_CONTENT)
    }

    @Test
    void "put"() {
        mockUser('test1', 't1', 'pwd')
        AVO entity = new AVO()
        entity.setTest(1)
        AVO result = resOfPost(URL, JSONUtil.toJSON(entity), HttpStatus.CREATED)
        result.setTest(2)
        AVO result2 = resOfPut(URL, JSONUtil.toJSON(result), HttpStatus.OK)
        expect:
        assert 2 == result2.getTest()
    }

    @Test
    void "get"() {
        mockUser('test1', 't1', 'pwd')
        AVO entity1 = new AVO().setTest(1)
        entity1.setVoStr("123132")
        AVO entity2 = new AVO().setTest(2)
        entity2.setVoStr("123132")
        AVO entity3 = new AVO().setTest(3)
        entity3.setVoStr("123132")
        resOfPost(URL, JSONUtil.toJSON(entity1), HttpStatus.CREATED)
        resOfPost(URL, JSONUtil.toJSON(entity2), HttpStatus.CREATED)
        resOfPost(URL, JSONUtil.toJSON(entity3), HttpStatus.CREATED)
        List<AVO> list = resOfGet(URL, HttpStatus.OK)
        DataTrunk<AVO> page = resOfGet(URL + "?pageNo=1&pageSize=1", HttpStatus.OK)
        expect:
        assert 3 == list.size()
        assert 3 == page.count
        assert 1 == page.getData().size()
    }

    /**
     * A,B,C关系
     * A与B manytomany
     * B与C onetoone
     * A与C ManyToOne
     */
    @Test
    @NoTx
    void "complex"() {
        //存B
        BVO resultB = resOfPost("/b", JSONUtil.toJSON(new BVO().setTest(1).setVoStrB("BBB")), HttpStatus.CREATED)
        BVO resultB2 = resOfPost("/b", JSONUtil.toJSON(new BVO().setTest(1).setVoStrB("BBB2")), HttpStatus.CREATED)

        CVO resultC2 = resOfPost("/c", JSONUtil.toJSON(new CVO().setTest(2)), HttpStatus.CREATED)
        //C存B
        CVO resultCB = resOfPost("/c/${resultC2.getId()}/b/${resultB.getId()}", JSONUtil.toJSON(new CVO()), HttpStatus.CREATED)
        assert resultCB.bvo.getVoStrB() == "BBB"
        //C有B不过没有显示调用所以懒加载不加载
        List<CVO> listC = JSONUtil.parse(get("/c", HttpStatus.OK).getResponse().getContentAsString(), List)
        assert listC.size() == 1
        assert listC.get(0).bvo == null
        //存C
        CVO resultC = resOfPost("/c", JSONUtil.toJSON(new CVO().setTest(1)), HttpStatus.CREATED)
        AVO entity1 = new AVO().setTest(1).setVoStr("AAA")
        AVO avo = resOfPost(URL, JSONUtil.toJSON(entity1), HttpStatus.CREATED)
        //A关联C
        AVO avoc = resOfPost(URL + "/" + avo.getId() + "/c/" + resultC.getId(), JSONUtil.toJSON(entity1), HttpStatus.CREATED)
        assert avoc.bvos.size() == 0
        assert avoc.cvo.getTest() == 1
        AVO avob = resOfPost(URL + "/" + avo.getId() + "/b/" + resultB.getId(), JSONUtil.toJSON(entity1), HttpStatus.CREATED)
        assert avob.cvo == null
        assert avob.bvos.size() == 1
        assert avob.bvos[0].voStrB == "BBB"
        AVO avob2 = resOfPost(URL + "/" + avo.getId() + "/b/" + resultB2.getId(), JSONUtil.toJSON(entity1), HttpStatus.CREATED)
        assert avob2.cvo == null
        assert avob2.bvos.size() == 2
        List<AVO> list = JSONUtil.parse(get(URL, HttpStatus.OK).getResponse().getContentAsString(), List)
        assert list.size() == 1
        assert list.get(0).bvos == null
        List<AVO> listBs = JSONUtil.parse(get(URL + "/bs", HttpStatus.OK).getResponse().getContentAsString(), List)
        listBs.size() == 1
        assert listBs.get(0).bvos.size() == 2
    }

    @Test
    @NoTx
    void oneToMany() {
        //存B
        BVO resultB = resOfPost("/b", JSONUtil.toJSON(new BVO().setTest(1).setVoStrB("BBB")), HttpStatus.CREATED)
        //存C
        CVO resultC = resOfPost("/c", JSONUtil.toJSON(new CVO().setTest(1)), HttpStatus.CREATED)
        CVO resultC2 = resOfPost("/c", JSONUtil.toJSON(new CVO().setTest(2)), HttpStatus.CREATED)
        //C存B
        CVO resultCB = resOfPost("/c/" + resultC2.getId() + "/b/" + resultB.getId(), JSONUtil.toJSON(new CVO()), HttpStatus.CREATED)
        AVO entity1 = new AVO().setTest(1).setVoStr("AAA")
        AVO avo = resOfPost(URL, JSONUtil.toJSON(entity1), HttpStatus.CREATED)
        //A关联C
        AVO avoc = resOfPost(URL + "/" + avo.getId() + "/c/" + resultC.getId(), JSONUtil.toJSON(entity1), HttpStatus.CREATED)
        AVO avob = resOfPost(URL + "/" + avo.getId() + "/b/" + resultB.getId(), JSONUtil.toJSON(entity1), HttpStatus.CREATED)
    }

    /**
     * 只读事务套可写则不可写
     */
    @Test
    @NoTx
    void readOnlyIncludeWriteTxTest() {
        arepository.deleteAll()
        crepository.deleteAll()
        brepository.deleteAll()
        BEntity b = new BEntity()
        b.setTest(2)
        BEntity bsave = resOfPost(URL + "/b", JSONUtil.toJSON(b), HttpStatus.CREATED)
        List bs = JSONUtil.parse(get("/b", HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert bs.size() == 0
    }


    @Test
    void deleteTest() {
        AVO entity1 = new AVO().setTest(1).setVoStr("AAA")
        AVO avo1 = resOfPost(URL, JSONUtil.toJSON(entity1), HttpStatus.CREATED)
        delete("/a?id=" + avo1.getId(), HttpStatus.NO_CONTENT)
        delete("/a?id=" + avo1.getId(), HttpStatus.NOT_FOUND)
    }

}
