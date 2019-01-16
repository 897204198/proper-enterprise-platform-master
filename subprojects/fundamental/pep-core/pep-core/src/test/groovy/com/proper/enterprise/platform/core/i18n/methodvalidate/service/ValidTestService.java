package com.proper.enterprise.platform.core.i18n.methodvalidate.service;

import com.proper.enterprise.platform.core.i18n.methodvalidate.TestBean;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Validated
public interface ValidTestService {

    /**
     * 测试
     *
     * @param a        a
     * @param testBean testBean
     */
    void test(@NotNull(message = "{core.test.key}") String a, @Valid TestBean testBean);

    /**
     * 测试
     *
     * @param a        a
     * @param testBean testBean
     */
    @Validated(TestBean.MinValidGroup.class)
    void testGroup(@NotNull(message = "{core.test.key}") String a, @Valid TestBean testBean);

    /**
     * 测试
     *
     * @param a a
     */
    void testMapFail(@NotEmpty(message = "{sys.test.key}") Map a);

    /**
     * 测试
     *
     * @param b b
     */
    void testMapSuccess(@NotEmpty(message = "{core.test.key}") Map<String, Object> b);
}
