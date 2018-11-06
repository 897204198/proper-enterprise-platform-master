package com.proper.enterprise.platform.announcement.controller

import com.proper.enterprise.platform.announcement.entity.AnnouncementEntity
import com.proper.enterprise.platform.announcement.service.AnnouncementService
import com.proper.enterprise.platform.core.utils.DateUtil
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.sys.i18n.I18NService
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

@Sql("/com/proper/enterprise/platform/announcement/controller/announcement.sql")
class AdminAnnouncementControllerTest extends AbstractTest{
    @Autowired
    private AnnouncementService announcementService

    @Autowired
    private I18NService i18NService

    def pageReq = "&pageNo=1&pageSize=10"

    @Test
    void testGetAnnouncementByInfoType() {

        List<AnnouncementEntity> list = announcementService.findAll()
        assert list.size() == 5

        def url = "/admin/announcement?infoType="
        def result1 = resOfGet("${url}ACTIVITY_INFORMATION$pageReq", HttpStatus.OK)

        assert result1.data.size() == 4
        assert result1.data[0].title == "医改公告"
        assert result1.data[0].info.contains("按照国家和省深化医药卫生体制改革总体部署和要求")
        assert result1.data[0].beginTime == "2016-12-01 00:00:00"

        def result2 = resOfGet("${url}NOTICE_INFORMATION$pageReq", HttpStatus.OK)

        assert result2.data.size() == 1
        assert result2.data[0].title == "测试公告通知"
        assert result2.data[0].info.contains("测试公告通知内容")
        assert result2.data[0].beginTime == "2017-02-01 00:00:00"

        def result3 = resOfGet("${url}NOTHING$pageReq", HttpStatus.OK)
        assert result3.data.size() == 0

        def result4 = resOfGet("${url}ACTIVITY_INFORMATION&title=公告$pageReq", HttpStatus.OK)

        assert result4.data.size() == 1
        assert result4.data[0].title == "医改公告"
        assert result4.data[0].info.contains("按照国家和省深化医药卫生体制改革总体部署和要求")
        assert result4.data[0].beginTime == "2016-12-01 00:00:00"

        def result5 = resOfGet("${url}ACTIVITY_INFORMATION&title=公告", HttpStatus.OK)
        assert result5.count == 1
        assert result5.data.get(0).title == "医改公告"
        assert result5.data.get(0).info.contains("按照国家和省深化医药卫生体制改革总体部署和要求")
        assert result5.data.get(0).beginTime == "2016-12-01 00:00:00"
    }

    @Test
    void testAddAnnouncement() {
        AnnouncementEntity announcementEntity = new AnnouncementEntity()
        announcementEntity.setInfoType("ACTIVITY_INFORMATION")
        announcementEntity.setInfo("测试添加一条信息")
        announcementEntity.setTitle("测试标题")
        announcementEntity.setBeginTime(DateUtil.addDay(new Date(), -1).format('yyyy-MM-dd HH:mm:ss'))
        announcementEntity.setEndTime(DateUtil.addDay(new Date(), 1).format('yyyy-MM-dd HH:mm:ss'))
        post('/admin/announcement', JSONUtil.toJSON(announcementEntity), HttpStatus.CREATED)
        def result4 = resOfGet('/admin/announcement?infoType=ACTIVITY_INFORMATION&title=测试标&pageNo=1&pageSize=10', HttpStatus.OK)
        assert result4.count == 1
        assert result4.data[0].title == "测试标题"
        assert result4.data[0].info.contains("测试添加一条信息")
        assert result4.data[0].infoStatus == false
    }

    @Test
    void testModifyAnnouncementEntity() {
        String id = '90498ccc-95d6-11e6-83da-fbb35e195804'

        def result = resOfGet("/admin/announcement?infoType=NOTICE_INFORMATION&title=测试公告通知$pageReq", HttpStatus.OK)
        assert result.data.size() == 1
        assert result.data[0].title == "测试公告通知"
        assert result.data[0].info.contains("测试公告通知内容")
        assert result.data[0].infoStatus == true

        AnnouncementEntity announcementEntity = new AnnouncementEntity()
        announcementEntity.setInfoType("NOTICE_INFORMATION")
        announcementEntity.setInfo("测试添加一条信息111")
        announcementEntity.setTitle("测试标题1")
        announcementEntity.setBeginTime("2017-11-15 17:26:00")
        announcementEntity.setEndTime("2020-11-15 19:26:00")
        announcementEntity.setInfoStatus(true)

        put('/admin/announcement/' + id, JSONUtil.toJSON(announcementEntity), HttpStatus.OK)

        result = resOfGet("/admin/announcement?infoType=NOTICE_INFORMATION&title=标题1$pageReq", HttpStatus.OK)
        assert result.data.size() == 1
        assert result.data[0].title == "测试标题1"
        assert result.data[0].info.contains("测试添加一条信息111")
        assert result.data[0].infoStatus == true

        delete('/admin/announcement/' + id, HttpStatus.NO_CONTENT)
        result = resOfGet("/admin/announcement?infoType=NOTICE_INFORMATION&title=标题1$pageReq", HttpStatus.OK)
        assert result.count == 0
    }
}
