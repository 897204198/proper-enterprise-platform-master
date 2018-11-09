package com.proper.enterprise.platform.notice.server.push.controller

import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.notice.server.sdk.enums.PushChannelEnum
import com.proper.enterprise.platform.notice.server.push.vo.PushNoticeMsgVO
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Test
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

class PushNoticeMsgControllerTest extends AbstractTest {

    @Test
    @Sql([
        "/com/proper/enterprise/platform/notice/server/push/statistic/push-msg-findByDateTypeAndAppKeyDay.sql"
    ])
    public void getTest() {
        DataTrunk<PushNoticeMsgVO> pushNoticeMsgVODataTrunk = JSONUtil.parse(get("/notice/server/push?pageNo=1&pageSize=10", HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)

        assert pushNoticeMsgVODataTrunk.getCount() == 6
        assert pushNoticeMsgVODataTrunk.getData()[0].id == "m_id_6"
        assert pushNoticeMsgVODataTrunk.getData()[0].sendDate == "2018-07-25 19:07:56"
        assert pushNoticeMsgVODataTrunk.getData()[0].pushChannel == PushChannelEnum.APNS.name()

    }
}
