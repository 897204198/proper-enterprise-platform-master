package com.proper.enterprise.platform.sys.controller

import com.proper.enterprise.platform.core.PEPConstants
import com.proper.enterprise.platform.core.model.CurrentModel
import com.proper.enterprise.platform.core.utils.DateUtil
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Test
import org.springframework.http.HttpStatus

class SystemControllerTest extends AbstractTest {

    @Test
    public void testCurrentDate() {
        CurrentModel currentDate = JSONUtil.parse(get("/sys/current/date", HttpStatus.OK).getResponse().getContentAsString(), CurrentModel.class)
        assert currentDate.getData() < new Date().getTime()
        assert DateUtil.toDate(currentDate.getId(), PEPConstants.DEFAULT_DATETIME_FORMAT).getTime() <= new Date().getTime()
    }
}
