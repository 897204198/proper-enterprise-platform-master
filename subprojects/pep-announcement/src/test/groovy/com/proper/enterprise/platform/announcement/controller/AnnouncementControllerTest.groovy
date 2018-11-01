package com.proper.enterprise.platform.announcement.controller

import com.proper.enterprise.platform.announcement.entity.AnnouncementEntity
import com.proper.enterprise.platform.announcement.service.AnnouncementService
import com.proper.enterprise.platform.sys.i18n.I18NService
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

@Sql("/com/proper/enterprise/platform/announcement/controller/announcement.sql")
class AnnouncementControllerTest extends AbstractTest {

    @Autowired
    private AnnouncementService announcementService

    @Autowired
    private I18NService i18NService

    @Test
    void testGetAnnouncementByInfoType() {

        List<AnnouncementEntity> noticeInfoList = announcementService.findAll()
        assert noticeInfoList.size() == 5

        def noticeUrl = "/sys/announcement?infoType="
        List<AnnouncementEntity> result1 = (List<AnnouncementEntity>) com.proper.enterprise.platform.core.utils.JSONUtil.parse(get(noticeUrl + 'ACTIVITY_INFORMATION', HttpStatus.OK).getResponse().getContentAsString(), List.class)

        assert result1.size() == 2
        assert result1.get(0).title == "医改公告"
        assert result1.get(0).info.contains("按照国家和省深化医药卫生体制改革总体部署和要求")
        assert result1.get(0).beginTime == "2016-12-01 00:00:00"

        def result2 = (List<AnnouncementEntity>) com.proper.enterprise.platform.core.utils.JSONUtil.parse(get(noticeUrl + 'NOTICE_INFORMATION', HttpStatus.OK).getResponse().getContentAsString(), List.class)

        assert result2.size() == 1
        assert result2.get(0).title == "测试公告通知"
        assert result2.get(0).info.contains("测试公告通知内容")
        assert result2.get(0).beginTime == "2017-02-01 00:00:00"


        def result3 = (List<AnnouncementEntity>) JSONUtil.parse(get(noticeUrl + 'NOTHING', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert result3.size() == 0
    }

    @Test
    void testGetAnnouncementTypes() {
        def result = (List<Map<String, String>>) JSONUtil.parse(get('/sys/announcement/types', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert result.size() == 2
        assert result.get(0).get("infoType") == 'ACTIVITY_INFORMATION'
        assert result.get(0).get("infoDescribe") == i18NService.getMessage("pep.sys.popup.sms.type")
        assert result.get(1).get("infoType") == 'NOTICE_INFORMATION'
        assert result.get(1).get("infoDescribe") == i18NService.getMessage("pep.sys.scroll.sms.type")

    }
}