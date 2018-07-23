package com.proper.enterprise.platform.notice.service

import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class NoticeServiceImplTest extends AbstractTest {

    @Autowired
    NoticeService noticeService

    @Test
    void findNoticesByNoticeChannelName() {
        def result = JSONUtil.parse(get('/notice/PUSH', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert result.size() == 4
        result = JSONUtil.parse(get('/notice/SMS', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert result.size() == 4
        result = JSONUtil.parse(get('/notice/EMAIL', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert result.size() == 4
        result = JSONUtil.parse(get('/notice/NORMAL', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert result.size() == 2
        result = JSONUtil.parse(get('/notice/BULABULA', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert result.size() == 9
    }

    @Before
    void init() {
        Map<String, Object> custom = new HashMap<>(1)
        custom.put("pageUrl", "messages")
        def userIds = ['PEP1', 'PEP2']
        noticeService.sendNotice("testModule", "测试模块", "test1", "this is a test.", userIds, custom, "")
        noticeService.sendNotice("testModule", "测试模块", "test2", "this is a test.", "PEP", custom, "SMS ")
        noticeService.sendNotice("testModule", "测试模块", "test3", "this is a test.", "PEP", custom, "EMAIL ")
        noticeService.sendNotice("testModule", "测试模块", "test4", "this is a test.", "PEP", custom, "EMAIL SMS")
        noticeService.sendNotice("testModule", "测试模块", "test5", "this is a test.", "PEP", custom, "PUSH")
        noticeService.sendNotice("testModule", "测试模块", "test6", "this is a test.", "PEP", custom, "PUSH EMAIL")
        noticeService.sendNotice("testModule", "测试模块", "test7", "this is a test.", "PEP", custom, "PUSH SMS")
        noticeService.sendNotice("testModule", "测试模块", "test8", "this is a test.", "PEP", custom, "PUSH EMAIL SMS")
    }

}
