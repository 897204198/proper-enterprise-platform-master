package com.proper.enterprise.platform.core.jpa.curd

import com.proper.enterprise.platform.core.jpa.curd.a.repository.ARepository
import com.proper.enterprise.platform.core.jpa.curd.a.vo.AVO
import com.proper.enterprise.platform.core.jpa.curd.b.entity.BEntity
import com.proper.enterprise.platform.core.jpa.curd.b.repository.BRepository
import com.proper.enterprise.platform.core.jpa.curd.b.vo.BVO
import com.proper.enterprise.platform.core.jpa.curd.c.repository.CRepository
import com.proper.enterprise.platform.core.jpa.curd.c.vo.CVO
import com.proper.enterprise.platform.test.annotation.NoTx
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.utils.JSONUtil


class CRUDTest extends AbstractTest {

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
        AVO result = postAndReturn(URL, new AVO())
        expect:
        assert null != result.getId()
    }

    @Test
    void "delete"() {
        mockUser('test1', 't1', 'pwd')
        delete(URL + "?id=1", HttpStatus.NOT_FOUND)
        AVO avo = postAndReturn(URL, new AVO())
        delete(URL + "?id=" + avo.getId(), HttpStatus.NO_CONTENT)
    }

    @Test
    void "put"() {
        mockUser('test1', 't1', 'pwd')
        AVO entity = new AVO()
        entity.setTest(1)
        AVO result = postAndReturn(URL, entity)
        result.setTest(2)
        AVO result2 = JSONUtil.parse(put(URL, JSONUtil.toJSON(result), HttpStatus.OK)
            .getResponse().getContentAsString(), AVO.class)
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
        postAndReturn(URL, entity1)
        postAndReturn(URL, entity2)
        postAndReturn(URL, entity3)
        List<AVO> list = JSONUtil.parse(get(URL, HttpStatus.OK).getResponse().getContentAsString(), List)
        DataTrunk<AVO> page = JSONUtil.parse(get(URL + "?pageNo=1&pageSize=1", HttpStatus.OK).getResponse().getContentAsString(), DataTrunk)
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
        BVO resultB = postAndReturn("/b", new BVO().setTest(1).setVoStrB("BBB"))
        BVO resultB2 = postAndReturn("/b", new BVO().setTest(1).setVoStrB("BBB2"))

        CVO resultC2 = postAndReturn("/c", new CVO().setTest(2))
        //C存B
        CVO resultCB = postAndReturn("/c/" + resultC2.getId() + "/b/" + resultB.getId(), new CVO())
        assert resultCB.bvo.getVoStrB() == "BBB"
        //C有B不过没有显示调用所以懒加载不加载
        List<CVO> listC = JSONUtil.parse(get("/c", HttpStatus.OK).getResponse().getContentAsString(), List)
        assert listC.size() == 1
        assert listC.get(0).bvo == null
        //存C
        CVO resultC = postAndReturn("/c", new CVO().setTest(1))
        AVO entity1 = new AVO().setTest(1).setVoStr("AAA")
        AVO avo = postAndReturn(URL, entity1)
        //A关联C
        AVO avoc = postAndReturn(URL + "/" + avo.getId() + "/c/" + resultC.getId(), entity1)
        assert avoc.bvos.size() == 0
        assert avoc.cvo.getTest() == 1
        AVO avob = postAndReturn(URL + "/" + avo.getId() + "/b/" + resultB.getId(), entity1)
        assert avob.cvo == null
        assert avob.bvos.size() == 1
        assert avob.bvos[0].getVoStrB() == "BBB"
        AVO avob2 = postAndReturn(URL + "/" + avo.getId() + "/b/" + resultB2.getId(), entity1)
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
        BVO resultB = postAndReturn("/b", new BVO().setTest(1).setVoStrB("BBB"))
        //存C
        CVO resultC = postAndReturn("/c", new CVO().setTest(1))
        CVO resultC2 = postAndReturn("/c", new CVO().setTest(2))
        //C存B
        CVO resultCB = postAndReturn("/c/" + resultC2.getId() + "/b/" + resultB.getId(), new CVO())
        AVO entity1 = new AVO().setTest(1).setVoStr("AAA")
        AVO avo = postAndReturn(URL, entity1)
        //A关联C
        AVO avoc = postAndReturn(URL + "/" + avo.getId() + "/c/" + resultC.getId(), entity1)
        AVO avob = postAndReturn(URL + "/" + avo.getId() + "/b/" + resultB.getId(), entity1)
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
        BEntity bsave = postAndReturn(URL + "/b", b)
        List bs = JSONUtil.parse(get("/b", HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert bs.size() == 0
    }


    @Test
    void deleteTest() {
        AVO entity1 = new AVO().setTest(1).setVoStr("AAA")
        AVO avo1 = postAndReturn(URL, entity1)
        delete("/a?id=" + avo1.getId(), HttpStatus.NO_CONTENT)
        delete("/a?id=" + avo1.getId(), HttpStatus.NOT_FOUND)
    }

}
