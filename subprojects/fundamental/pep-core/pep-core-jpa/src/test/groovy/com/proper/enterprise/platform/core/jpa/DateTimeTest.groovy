package com.proper.enterprise.platform.core.jpa

import com.proper.enterprise.platform.core.CoreProperties
import com.proper.enterprise.platform.core.PEPPropertiesLoader
import com.proper.enterprise.platform.core.jpa.entity.DateTimeEntity
import com.proper.enterprise.platform.core.utils.DateUtil
import com.proper.enterprise.platform.test.AbstractSpringTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Test
import org.springframework.http.HttpStatus

class DateTimeTest extends AbstractSpringTest {

    private static final URL = "/datetime"

    @Test
    void "post"() {
        mockUser('test1', 't1', 'pwd')
        def configReq = [:]
        configReq['date'] = '2018-07-25T01:42:17.582Z'
        DateTimeEntity result = JSONUtil.parse(post(URL, JSONUtil.toJSON(configReq),
            HttpStatus.CREATED).getResponse().getContentAsString(), DateTimeEntity.class)
        expect:
        assert "2018-07-25 09:42:17" == DateUtil.toString(result.getDate(), PEPPropertiesLoader.load(CoreProperties.class).getDefaultDatetimeFormat())
    }

}
