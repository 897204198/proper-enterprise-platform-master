package com.proper.enterprise.platform.notice.util

import com.proper.enterprise.platform.api.auth.model.User
import com.proper.enterprise.platform.auth.common.jpa.entity.UserEntity
import com.proper.enterprise.platform.notice.document.NoticeDocument
import com.proper.enterprise.platform.notice.entity.PushDeviceEntity
import com.proper.enterprise.platform.notice.enums.AnalysisResult
import com.proper.enterprise.platform.notice.enums.PushDeviceType
import com.proper.enterprise.platform.test.AbstractSpringTest
import org.junit.Test

class NoticeAnalysisUtilTest extends AbstractSpringTest {

    @Test
    void isDeviceInfoOk() {
        NoticeDocument noticeDocument = new NoticeDocument()
        UserEntity userEntity = new UserEntity()
        userEntity.setName("测试人员")
        User user = userEntity
        PushDeviceEntity pushDeviceEntity = new PushDeviceEntity()
        pushDeviceEntity.setPushToken(null)
        NoticeAnalysisUtil.isDeviceInfoOk(noticeDocument, user, pushDeviceEntity)
        assert noticeDocument.getAnalysisResult() == AnalysisResult.PARTLY
        assert noticeDocument.getNotes().size() == 3
        assert noticeDocument.getNotes().get(0) == "测试人员 is missing push token, please re login to the app."
        assert noticeDocument.getNotes().get(1) == "测试人员 is missing device type, please re login to the app."
        assert noticeDocument.getNotes().get(2) == "测试人员 is missing push mode, please re login to the app."

        noticeDocument = new NoticeDocument()
        userEntity = new UserEntity()
        userEntity.setName("测试人员2")
        user = userEntity
        pushDeviceEntity = new PushDeviceEntity()
        pushDeviceEntity.setPushMode("pushModel")
        pushDeviceEntity.setDeviceType(PushDeviceType.ios)
        pushDeviceEntity.setPushToken("")
        NoticeAnalysisUtil.isDeviceInfoOk(noticeDocument, user, pushDeviceEntity)
        assert noticeDocument.getAnalysisResult() == AnalysisResult.PARTLY
        assert noticeDocument.getNotes().size() == 1
        assert noticeDocument.getNotes().get(0) == "测试人员2 is missing push token, please re login to the app."
    }

}
