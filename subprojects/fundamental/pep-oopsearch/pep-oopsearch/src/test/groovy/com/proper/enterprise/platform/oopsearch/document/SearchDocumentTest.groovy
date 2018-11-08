package com.proper.enterprise.platform.oopsearch.document

import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Test

class SearchDocumentTest extends AbstractJPATest{

    @Test
    void searchDocument() {
        SearchDocument searchDocument = new SearchDocument()
        searchDocument.setCon("1")
        searchDocument.setCol("1")
        searchDocument.setDes("1")
        searchDocument.setTab("1")
        searchDocument.setPri("1")
        searchDocument.getCon()
        searchDocument.getCol()
        searchDocument.getDes()
        searchDocument.getTab()
        searchDocument.getPri()
    }
}
