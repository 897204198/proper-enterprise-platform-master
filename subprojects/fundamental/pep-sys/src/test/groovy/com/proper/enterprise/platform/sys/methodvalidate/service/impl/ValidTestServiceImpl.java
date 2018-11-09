package com.proper.enterprise.platform.sys.methodvalidate.service.impl;

import com.proper.enterprise.platform.sys.methodvalidate.TestBean;
import com.proper.enterprise.platform.sys.methodvalidate.service.ValidTestService;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Service
public class ValidTestServiceImpl implements ValidTestService {

    @Override
    public void test(@NotNull(message = "{sys.test.key}") String a, @Valid TestBean testBean) {

    }

    @Override
    public void testGroup(@NotNull(message = "{sys.test.key}") String a, @Valid TestBean testBean) {

    }

    @Override
    public void testMapFail(Map b) {

    }

    @Override
    public void testMapSuccess(Map<String, Object> b) {

    }
}
