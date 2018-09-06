package com.proper.enterprise.platform.notice.server.app.controller

import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.notice.server.app.dao.repository.NoticeRepository
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

class NoticeServerControllerTest extends AbstractTest {

    @Autowired
    private NoticeRepository noticeRepository

    @Test
    @Sql("/com/proper/enterprise/platform/notice/server/app/dao/sql/queryNotices.sql")
    public void getTest() {
        List searchList = JSONUtil.parse(get("/notice/server?id=1&appKey=1&batchId=1&targetTo=1&content=1&noticeType=EMAIL&status=RETRY",
            HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert searchList.size() == 1

        DataTrunk dataTrunk = JSONUtil.parse(get("/notice/server?pageNo=1&pageSize=2&noticeType=EMAIL&status=RETRY",
            HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert dataTrunk.getCount() == 3
        assert dataTrunk.getData().size() == 2
        assert dataTrunk.getData().get(0).id == '1'
    }
}
