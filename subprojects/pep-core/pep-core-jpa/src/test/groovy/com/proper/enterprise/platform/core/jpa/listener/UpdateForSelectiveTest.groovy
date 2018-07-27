package com.proper.enterprise.platform.core.jpa.listener

import com.proper.enterprise.platform.core.jpa.entity.OneEntity
import com.proper.enterprise.platform.core.jpa.service.OneService
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class UpdateForSelectiveTest extends AbstractTest {

    @Autowired
    OneService oneService

//单表的更新  仅更新非空字段

    @Test
    void updateForSelective() {
        OneEntity saveOne = oneService.save(new OneEntity().setTest(1).setChange(2))
        OneEntity updateOne = new OneEntity()
        updateOne.setChange(3).setId(saveOne.getId())
        updateOne = oneService.updateForSelective(updateOne)
        assert updateOne.getId() == saveOne.getId()
        assert updateOne.getTest() == saveOne.getTest()
        assert updateOne.getChange() == 3
    }

}
