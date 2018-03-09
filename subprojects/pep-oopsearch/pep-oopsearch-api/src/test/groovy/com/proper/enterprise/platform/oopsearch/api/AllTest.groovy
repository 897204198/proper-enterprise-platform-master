package com.proper.enterprise.platform.oopsearch.api

import com.proper.enterprise.platform.oopsearch.api.document.SearchDocument
import com.proper.enterprise.platform.oopsearch.api.model.SearchColumnModel
import com.proper.enterprise.platform.oopsearch.api.model.SyncDocumentModel
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test

class AllTest extends AbstractTest{

    @Test
    void testAbstractSearchConfigs(){
        String searchTables = "demo_dept"
        String searchColumns = "demo_dept:dept_id:string:部门id,demo_dept:dept_name:string:部门名称"
        int limit = 10
        String extendByYear = "去年,今年,明年"
        String extendByMonth = "上月,本月,下月"
        String extendByDay = "昨天,今天,明天"
        TestConfigs testConfigs = new TestConfigs(searchTables, searchColumns, limit, extendByYear, extendByMonth, extendByDay)
        def r1 = testConfigs.getSearchColumnListByType("string")
        assert r1.size() == 2
        def r2 = testConfigs.getSearchTableColumnMap()
        assert r2.size() == 1
        def r3 = testConfigs.getTableNameList()
        assert r3.size() == 1
        def r4 = testConfigs.getSearchColumnListByTable("demo_dept")
        assert r4.size() == 2
        def r5 = testConfigs.getLimit()
        assert r5 == 10
        def r6 = testConfigs.getExtendByYearArr()
        assert r6.length == 3
        def r7 = testConfigs.getExtendByMonthArr()
        assert r7.length == 3
        def r8 = testConfigs.getExtendByDayArr()
        assert r8.length == 3
    }

    @Test
    void testSearchDocument(){
        SearchDocument searchDocument = new SearchDocument()
        searchDocument.setPri("pri")
        assert searchDocument.getPri() == "pri"
        searchDocument.setDes("des")
        assert searchDocument.getDes() == "des"
        searchDocument.setCol("col")
        assert searchDocument.getCol() == "col"
        searchDocument.setTab("tab")
        assert searchDocument.getTab() == "tab"
        searchDocument.setCon("con")
        assert searchDocument.getCon() == "con"
    }

    @Test
    void testSearchColumnModel(){
        SearchColumnModel searchColumnModel = new SearchColumnModel()
        searchColumnModel.setTable("tab")
        assert searchColumnModel.getTable() == "tab"
        searchColumnModel.setColumn("col")
        assert searchColumnModel.getColumn() == "col"
        searchColumnModel.setDescColumn("des")
        assert searchColumnModel.getDescColumn() == "des"
        searchColumnModel.setType("type")
        assert searchColumnModel.getType() == "type"
    }

    @Test
    void testSyncDocumentModel(){
        SyncDocumentModel syncDocumentModel = new SyncDocumentModel()
        syncDocumentModel.setTab("tab")
        assert syncDocumentModel.getTab() == "tab"
        syncDocumentModel.setCol("col")
        assert syncDocumentModel.getCol() == "col"
        syncDocumentModel.setDes("des")
        assert syncDocumentModel.getDes() == "des"
        syncDocumentModel.setPri("pri")
        assert syncDocumentModel.getPri() == "pri"
        syncDocumentModel.setConb("conb")
        assert syncDocumentModel.getConb() == "conb"
        syncDocumentModel.setCona("cona")
        assert syncDocumentModel.getCona() == "cona"
    }
}
