package com.proper.enterprise.platform.workflow

import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.http.HttpStatus


class ValidationTest extends AbstractTest {

    @Test
    public void checkPluginsXml() {
        def res = get('/workflow/editor/editor-app/plugins.xml', HttpStatus.OK).getResponse()
        assert res.getContentType() == 'application/xml'
    }

}
