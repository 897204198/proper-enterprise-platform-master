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
        CustomGridOrderDocument customGridOrderDocument = new CustomGridOrderDocument()
        customGridOrderDocument.setField('f')
        assert customGridOrderDocument.getField() ==  'f'

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


    }


}
