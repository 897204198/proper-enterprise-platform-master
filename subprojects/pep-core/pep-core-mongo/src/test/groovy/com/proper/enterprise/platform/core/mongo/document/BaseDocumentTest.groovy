package com.proper.enterprise.platform.core.mongo.document

import com.proper.enterprise.platform.core.mongo.dao.MongoDAO
import com.proper.enterprise.platform.core.mongo.document.mock.document.MockDocument
import com.proper.enterprise.platform.core.mongo.document.mock.repository.MockRepository
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class BaseDocumentTest extends AbstractTest {

    @Autowired
    MockRepository repository

    @Autowired
    MongoDAO dao

    @Test
    public void ignorePropertyInDocument() {
        def r = get('/core/mongo/test/json/document', HttpStatus.OK).getResponse().getContentAsString()
        def m = JSONUtil.parse(r, Map.class)
        assert m.size() == 3
        assert m.containsKey('id')
        assert m.containsKey('createTime')
        assert m.containsKey('docC2')
    }

    @Test
    public void checkMongoField() {
        def mock = repository.save(new MockDocument('c1', 'c2'))
        def id = mock.getId()
        def doc = dao.queryById('mock', id)

        assert doc.get('CT')
        assert doc.getString('LT') == mock.getLastModifyTime()
        assert doc.get('CU') == "PEP_SYS"
        assert mock.getCreateUserId() == "PEP_SYS"
        assert mock.getLastModifyUserId() == "PEP_SYS"
        assert doc.getString('docC1') == 'c1'
        assert doc.getString('C2') == 'c2'

        dao.drop('mock')
    }

}
