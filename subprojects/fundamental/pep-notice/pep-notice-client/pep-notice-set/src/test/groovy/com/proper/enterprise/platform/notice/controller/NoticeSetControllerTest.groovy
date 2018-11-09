package com.proper.enterprise.platform.notice.controller

import com.proper.enterprise.platform.core.security.Authentication
import com.proper.enterprise.platform.notice.document.NoticeSetDocument
import com.proper.enterprise.platform.notice.repository.NoticeSetRepository
import com.proper.enterprise.platform.notice.service.NoticeSetService
import com.proper.enterprise.platform.sys.datadic.entity.DataDicEntity
import com.proper.enterprise.platform.sys.datadic.enums.DataDicTypeEnum
import com.proper.enterprise.platform.sys.datadic.repository.DataDicRepository
import com.proper.enterprise.platform.test.AbstractJPATest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class NoticeSetControllerTest extends AbstractJPATest {

    @Autowired
    NoticeSetService noticeSetService

    @Autowired
    NoticeSetRepository noticeSetRepository

    @Autowired
    DataDicRepository dataDicRepository

    @Test
    void findAndSave() {

        DataDicEntity dataDicEntity = new DataDicEntity()
        dataDicEntity.setCatalog("NOTICE_CATALOG")
        dataDicEntity.setCode("SYSTEM")
        dataDicEntity.setDataDicType(DataDicTypeEnum.SYSTEM)
        dataDicEntity.setName("系统消息")
        dataDicEntity.setOrder(0)
        dataDicEntity.setEnable(true)
        dataDicEntity.setDeft(true)
        dataDicRepository.save(dataDicEntity)

        dataDicEntity = new DataDicEntity()
        dataDicEntity.setCatalog("NOTICE_CATALOG")
        dataDicEntity.setCode("BPM")
        dataDicEntity.setDataDicType(DataDicTypeEnum.SYSTEM)
        dataDicEntity.setName("工作流消息")
        dataDicEntity.setOrder(1)
        dataDicEntity.setEnable(true)
        dataDicEntity.setDeft(false)
        dataDicRepository.save(dataDicEntity)

        dataDicEntity = new DataDicEntity()
        dataDicEntity.setCatalog("NOTICE_CATALOG")
        dataDicEntity.setCode("IM")
        dataDicEntity.setDataDicType(DataDicTypeEnum.SYSTEM)
        dataDicEntity.setName("即时通讯消息")
        dataDicEntity.setOrder(2)
        dataDicEntity.setEnable(true)
        dataDicEntity.setDeft(false)
        dataDicRepository.save(dataDicEntity)

        dataDicEntity = new DataDicEntity()
        dataDicEntity.setCatalog("NOTICE_CATALOG")
        dataDicEntity.setCode("ELSE")
        dataDicEntity.setDataDicType(DataDicTypeEnum.SYSTEM)
        dataDicEntity.setName("其他消息")
        dataDicEntity.setOrder(3)
        dataDicEntity.setEnable(true)
        dataDicEntity.setDeft(false)
        dataDicRepository.save(dataDicEntity)

        NoticeSetDocument noticeSetDocument1 = new NoticeSetDocument()
        noticeSetDocument1.setUserId("test1")
        noticeSetDocument1.setCatalog("ELSE")
        noticeSetDocument1.noticeChannel = ["sms","push","email"]
        noticeSetRepository.save(noticeSetDocument1)

        mockUser('test1', 't1', 'pwd')
        Authentication.setCurrentUserId('test1')

        List<NoticeSetDocument> list = JSONUtil.parse(get("/notice/set", HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert list.size() == 4
        for (NoticeSetDocument temp : list) {
            temp.noticeChannel = []
            NoticeSetDocument resp = JSONUtil.parse(post("/notice/set", JSONUtil.toJSON(temp), HttpStatus.OK).getResponse().getContentAsString(), NoticeSetDocument)
            assert resp.getId() != null
        }
        list = JSONUtil.parse(get("/notice/set", HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert list.size() == 4
        for (NoticeSetDocument temp : list) {
            assert temp.noticeChannel == []
        }
    }


}
