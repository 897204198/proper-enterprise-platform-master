package com.proper.enterprise.platform.sys.methodvalidate

import com.proper.enterprise.platform.sys.i18n.I18NUtil
import com.proper.enterprise.platform.sys.methodvalidate.service.ValidTestService
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

import javax.validation.ConstraintViolationException

class MethodValidateTest extends AbstractTest {

    @Autowired
    public ValidTestService validTestService

    @Test
    public void testMethod() {
        TestBean testBean = new TestBean()
        try {
            validTestService.test(null, testBean)
        } catch (ConstraintViolationException e) {
            assert e.getMessage().contains(I18NUtil.getMessage("sys.test.key"))
        }
        try {
            validTestService.test("abc", testBean)
        } catch (ConstraintViolationException e) {
            assert e.getMessage().contains("sys.test.k3")
        }
        testBean.setAt("abc")
        testBean.setBt(99)
        try {
            validTestService.test("abc", testBean)
        } catch (ConstraintViolationException e) {
            assert e.getMessage().contains("min 100")
        }
        testBean.setBt(101)
        validTestService.test("abc", testBean)


        TestBean testBeanGroup = new TestBean()
        testBeanGroup.setBt(1)
        try {
            validTestService.testGroup(null, testBean)
        } catch (ConstraintViolationException e) {
            assert e.getMessage().contains("min 100")
        }
        testBeanGroup.setBt(111)
        validTestService.testGroup(null, testBean)
    }


    @Test
    public void testController() {
        TestBean testBean = new TestBean()
        assert I18NUtil.getMessage("sys.test.key") == post("/validtest", JSONUtil.toJSON(testBean), HttpStatus.INTERNAL_SERVER_ERROR)
            .getResponse()
            .getContentAsString()
    }
}
