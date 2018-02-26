package com.proper.enterprise.platform.search.common.document

import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test

class SearchDocumentTest extends AbstractTest{

    @Test
    void searchDocument() {
        SearchDocument searchDocument = new SearchDocument()
        searchDocument.setCon("1")
        searchDocument.setCol("1")
        searchDocument.setDes("1")
        searchDocument.setTab("1")
        searchDocument.getCon()
        searchDocument.getCol()
        searchDocument.getDes()
        searchDocument.getTab()
    }
}
