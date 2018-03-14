package com.proper.enterprise.platform.page.custom.grid.document

import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test

class DocumentTest extends AbstractTest {

    @Test
    void testAllDocument(){
        CustomGridDataDocument customGridDataDocument = new CustomGridDataDocument()
        customGridDataDocument.setTemplateUrl('u')
        assert customGridDataDocument.getTemplateUrl() == 'u'
        CustomGridDocument customGridDocument = new CustomGridDocument()
        customGridDocument.setTitle('t')
        customGridDocument.setCondition('c')
        customGridDocument.setValid(true)
        customGridDocument.setOrder(null)
        customGridDocument.setCode('code')
        assert customGridDocument.getTitle() == 't'
        assert customGridDocument.getCondition() == 'c'
        assert customGridDocument.isValid()
        assert customGridDocument.getOrder() == null
        assert customGridDocument.getCode() == 'code'
        customGridDocument.setPagination('pag')
        customGridDocument.getPagination() == 'pag'
        List<String> list12 = new ArrayList<String>()
        list12.add('type')
        customGridDocument.setOperationType(list12)
        assert customGridDocument.getOperationType().contains('type')
        List<CustomGridDataDocument> list = new ArrayList<>()
        CustomGridDataDocument customGridDataDocument1 = new CustomGridDataDocument()
        customGridDataDocument1.setName('name11')
        list.add(customGridDataDocument1)
        customGridDocument.setInitData(list)
        assert customGridDocument.getInitData().get(0).getName() == 'name11'
        List<CustomGridColumnDocument> documentList = new ArrayList<>()
        CustomGridColumnDocument customGridColumnDocument1 = new CustomGridColumnDocument()
        customGridColumnDocument1.setTitle('title')
        documentList.add(customGridColumnDocument1)
        customGridDocument.setColumns(documentList)
        assert customGridDocument.getColumns().get(0).getTitle() == 'title'
        List<CustomGridElementDocument> search = new ArrayList<>()
        CustomGridElementDocument customGridElementDocument1 = new CustomGridElementDocument()
        customGridElementDocument1.setName('name111')
        search.add(customGridElementDocument1)
        customGridDocument.setSearch(search)
        assert customGridDocument.getSearch().get(0).getName() == 'name111'
        List<CustomGridElementDocument> dialog = new ArrayList<>()
        CustomGridElementDocument customGridElementDocument2 = new CustomGridElementDocument()
        customGridElementDocument2.setName('name112')
        dialog.add(customGridElementDocument2)
        customGridDocument.setDialog(dialog)
        assert customGridDocument.getDialog().get(0).getName() == 'name112'
        customGridDocument.setIsAllDataLoad('data')
        assert customGridDocument.getIsAllDataLoad() == 'data'

        CustomGridOrderDocument customGridOrderDocument = new CustomGridOrderDocument()
        customGridOrderDocument.setOrder('order')
        assert customGridOrderDocument.getOrder() == 'order'
        customGridOrderDocument.setField('f')
        assert customGridOrderDocument.getField() ==  'f'
        customGridDataDocument.setName('name1')
        assert customGridDataDocument.getName() == 'name1'
        customGridDataDocument.setTemplate('temp1')
        assert customGridDataDocument.getTemplate() == 'temp1'

        CustomGridElementDocument customGridElementDocument = new CustomGridElementDocument()
        customGridElementDocument.setName('a')
        customGridElementDocument.setRow(1)
        customGridElementDocument.setModel('m')
        customGridElementDocument.setSelectCode('1')
        customGridElementDocument.setSelectData('2')
        customGridElementDocument.setType('type')
        customGridElementDocument.setSelectName('name')
        customGridElementDocument.setWidth('800')
        Collection<CustomGridValidDocument> emptyValidList = new ArrayList<>()
        customGridElementDocument.setValid(emptyValidList)
        customGridElementDocument.setCondition(null)

        assert customGridElementDocument.getName() == 'a'
        assert customGridElementDocument.getRow() == 1
        assert customGridElementDocument.getModel() == 'm'
        assert customGridElementDocument.getSelectCode() == '1'
        assert customGridElementDocument.getSelectData() == '2'
        assert customGridElementDocument.getType() == 'type'
        assert customGridElementDocument.getSelectName() == 'name'
        assert customGridElementDocument.getWidth() == '800'
        assert customGridElementDocument.getValid().size() == 0
        assert customGridElementDocument.getCondition() == null

        CustomGridFormatDocument customGridFormatDocument = new CustomGridFormatDocument()
        customGridFormatDocument.setType('type')
        customGridFormatDocument.setName('name')
        customGridFormatDocument.setCode('1')
        customGridFormatDocument.setDataName('data')

        assert customGridFormatDocument.getType() == 'type'
        assert customGridFormatDocument.getName() == 'name'
        assert customGridFormatDocument.getCode() == '1'
        assert customGridFormatDocument.getDataName() == 'data'

        CustomGridConditionDocument customGridConditionDocument = new CustomGridConditionDocument()
        customGridConditionDocument.setCategory('cat1')
        assert customGridConditionDocument.getCategory() == 'cat1'
        List<String> list11 = new ArrayList<>()
        list11.add('field1')
        customGridConditionDocument.setOrField(list11)
        assert customGridConditionDocument.getOrField().contains('field1')

        CustomGridColumnDocument customGridColumnDocument = new CustomGridColumnDocument()
        customGridColumnDocument.setField('field')
        assert customGridColumnDocument.getField() == 'field'
        customGridColumnDocument.setTitle('title')
        assert customGridColumnDocument.getTitle() == 'title'
        customGridColumnDocument.setAlign('align')
        assert customGridColumnDocument.getAlign() == 'align'
        customGridColumnDocument.setDisplayCharNum('charNum')
        assert customGridColumnDocument.getDisplayCharNum() == 'charNum'
        CustomGridFormatDocument customGridFormatDocument1 = new CustomGridFormatDocument()
        customGridFormatDocument1.setName('cus')
        customGridColumnDocument.setFormatter(customGridFormatDocument1)
        assert customGridColumnDocument.getFormatter().getName() == 'cus'

        CustomGridValidDocument customGridValidDocument = new CustomGridValidDocument()
        customGridValidDocument.setValidMode('valid')
        assert customGridValidDocument.getValidMode() == 'valid'
    }

}
