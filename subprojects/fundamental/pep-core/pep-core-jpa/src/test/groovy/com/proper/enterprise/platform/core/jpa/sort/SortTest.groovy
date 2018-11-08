package com.proper.enterprise.platform.core.jpa.sort

import com.proper.enterprise.platform.core.CoreProperties
import com.proper.enterprise.platform.core.PEPPropertiesLoader
import com.proper.enterprise.platform.core.domain.PEPOrder
import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.core.jpa.entity.OneEntity
import com.proper.enterprise.platform.core.jpa.service.OneService
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus

class SortTest extends AbstractJPATest {

    @Autowired
    private OneService oneService

    @Test
    public void sortTest() {
        OneEntity one = new OneEntity()
        one.setTest(1)
        one.setChange(2)
        OneEntity two = new OneEntity()
        two.setTest(2)
        two.setChange(1)

        oneService.save(one)
        oneService.save(two)

        List<OneEntity> list = oneService.findAll(new Sort(new Sort.Order(Sort.Direction.DESC, "test")))
        assert list.size() == 2
        assert list.get(0).getTest() == 2
        assert list.get(1).getTest() == 1


        List<OneEntity> list2 = oneService.findAll(new Sort(new Sort.Order(Sort.Direction.DESC, "change"), new Sort.Order(Sort.Direction.DESC, "test")))
        assert list2.size() == 2
        assert list2.get(0).getTest() == 1
        assert list2.get(1).getTest() == 2

        List<OneEntity> list3 = oneService.findAll(new Sort(new Sort.Order(Sort.Direction.DESC, "change")).and(new Sort(new Sort.Order(Sort.Direction.DESC, "test"))))
        assert list3.size() == 2
        assert list3.get(0).getTest() == 1
        assert list3.get(1).getTest() == 2

        List<PEPOrder> orders = new ArrayList<>()
        orders.add(new PEPOrder(Sort.Direction.DESC, "test"))

        DataTrunk<OneEntity> dataTrunk1 = JSONUtil.parse(get("/one?pageNo=1&pageSize=2&orders=" + URLEncoder.encode(JSONUtil.toJSON(orders), PEPPropertiesLoader.load(CoreProperties.class).getCharset()), HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert dataTrunk1.getData().size() == 2
        assert dataTrunk1.getData().get(0).test == 2
        assert dataTrunk1.getData().get(1).test == 1

        orders.clear()
        orders.add(new PEPOrder(Sort.Direction.DESC, "change"))
        orders.add(new PEPOrder(Sort.Direction.DESC, "test"))
        assert dataTrunk1.getData().size() == 2
        assert dataTrunk1.getData().get(0).change ==1
        assert dataTrunk1.getData().get(1).change == 2
    }
}
