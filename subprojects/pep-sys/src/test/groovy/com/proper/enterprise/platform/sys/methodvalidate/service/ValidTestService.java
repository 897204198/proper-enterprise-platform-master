package com.proper.enterprise.platform.sys.methodvalidate.service;

import com.proper.enterprise.platform.sys.methodvalidate.TestBean;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
public interface ValidTestService {

    /**
     * 测试
     *
     * @param a        a
     * @param testBean testBean
     */
    void test(@NotNull(message = "{sys.test.key}") String a, @Valid TestBean testBean);

    /**
     * 测试
     *
     * @param a        a
     * @param testBean testBean
     */
    @Validated(TestBean.MinValidGroup.class)
    void testGroup(@NotNull(message = "{sys.test.key}") String a, @Valid TestBean testBean);
}
