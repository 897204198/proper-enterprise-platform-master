package com.proper.enterprise.platform.sys.methodvalidate

import com.proper.enterprise.platform.core.i18n.I18NUtil
import com.proper.enterprise.platform.sys.methodvalidate.service.ValidTestService
import com.proper.enterprise.platform.test.AbstractSpringTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

import javax.validation.ConstraintViolationException
import javax.validation.UnexpectedTypeException

class MethodValidateTest extends AbstractSpringTest {

    @Autowired
    public ValidTestService validTestService

    @Rule
    public ExpectedException expectedException = ExpectedException.none()

    @Test
    void testMethod() {
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

        def map = [:]
        try {
            validTestService.testMapSuccess(map)
        } catch (ConstraintViolationException e) {
            assert e.getMessage().contains(I18NUtil.getMessage("sys.test.key"))
        }
        map['test'] = 'test'
        validTestService.testMapSuccess(map)
    }

    @Test
    void testTypeException() throws UnexpectedTypeException {
        expectedException.expect(UnexpectedTypeException.class)
        expectedException.expectMessage("No validator could be found for type")
        def map = [:]
        validTestService.testMapFail(map)
    }

    @Test
    void testController() {
        TestBean testBean = new TestBean()
        assert I18NUtil.getMessage("sys.test.key") == post("/validtest", JSONUtil.toJSON(testBean), HttpStatus.INTERNAL_SERVER_ERROR)
            .getResponse()
            .getContentAsString()
    }
}
