package com.proper.enterprise.platform.notice.service

import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.sys.datadic.service.DataDicService
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

@Sql("/com/proper/enterprise/platform/notice/sql/datadics.sql")
class NoticeServiceImplTest extends AbstractTest {

    @Autowired
    NoticeService noticeService

    @Autowired
    DataDicService dataDicService

    @Test
    void queryResultWithinType() {
        Map<String, Object> custom = new HashMap<>(1)
        custom.put("pageUrl", "messages")
        Set<String> userIds = new HashSet<>()
        userIds.add("PEP1")
        userIds.add("PEP2")
        noticeService.sendNotice("testModule", "测试业务", "MESSAGE", "test1", "this is a test.", userIds, custom, "")
        noticeService.sendNotice("testModule", "测试业务", "MESSAGE", "test2", "this is a test.", "PEP", custom, "SMS ")
        noticeService.sendNotice("testModule", "测试业务", "MESSAGE", "test3", "this is a test.", "PEP", custom, "EMAIL ")
        noticeService.sendNotice("testModule", "测试业务", "MESSAGE", "test4", "this is a test.", "PEP", custom, "EMAIL SMS")
        noticeService.sendNotice("testModule", "测试业务", "MESSAGE", "test5", "this is a test.", "PEP", custom, "PUSH")
        noticeService.sendNotice("testModule", "测试业务", "MESSAGE", "test6", "this is a test.", "PEP", custom, "PUSH EMAIL")
        noticeService.sendNotice("testModule", "测试业务", "MESSAGE", "test7", "this is a test.", "PEP", custom, "PUSH SMS")
        noticeService.sendNotice("testModule", "测试业务", "MESSAGE", "test8", "this is a test.", "PEP", custom, "PUSH EMAIL SMS")

        def result = JSONUtil.parse(get('/notice/PUSH', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert result.size() == 4
        result = JSONUtil.parse(get('/notice/SMS', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert result.size() == 4
        result = JSONUtil.parse(get('/notice/EMAIL', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert result.size() == 4
        result = JSONUtil.parse(get('/notice/NORMAL', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert result.size() == 2

    }

    @Test
    void queryResultWithoutType() {
        Map<String, Object> custom = new HashMap<>(1)
        custom.put("pageUrl", "messages")
        Set<String> userIds = new HashSet<>()
        userIds.add("PEP1")
        userIds.add("PEP2")
        //Default
        noticeService.sendNotice("testBpm", "测试流程", "BPM", "bpm1", "this is a bpm test.", userIds, custom)
        def result = JSONUtil.parse(get('/notice/PUSH', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert result.size() == 2
        result = JSONUtil.parse(get('/notice/SMS', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert result.size() == 0

        noticeService.sendNotice("testBpm", "测试流程", "BPM", "bpm2", "this is a bpm test.", "test_bpm_user01", custom)
        result = JSONUtil.parse(get('/notice/PUSH', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert result.size() == 2
        result = JSONUtil.parse(get('/notice/SMS', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert result.size() == 1
        result = JSONUtil.parse(get('/notice/EMAIL', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert result.size() == 1
        result = JSONUtil.parse(get('/notice/NORMAL', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert result.size() == 0
    }

}
