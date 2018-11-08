package com.proper.enterprise.platform.sys.controller

import com.proper.enterprise.platform.core.CoreProperties
import com.proper.enterprise.platform.core.PEPPropertiesLoader
import com.proper.enterprise.platform.core.model.CurrentModel
import com.proper.enterprise.platform.core.utils.DateUtil
import com.proper.enterprise.platform.test.AbstractSpringTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Test
import org.springframework.http.HttpStatus

class SystemControllerTest extends AbstractSpringTest {

    @Test
    public void testCurrentDate() {
        CurrentModel currentDate = JSONUtil.parse(get("/sys/current/date", HttpStatus.OK).getResponse().getContentAsString(), CurrentModel.class)
        assert currentDate.getData() < new Date().getTime()
        assert DateUtil.toDate(currentDate.getId(), PEPPropertiesLoader.load(CoreProperties.class).getDefaultDatetimeFormat()).getTime() <= new Date().getTime()
    }
}
