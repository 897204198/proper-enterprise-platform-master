package com.proper.enterprise.platform.feedback.service

import com.proper.enterprise.platform.feedback.document.FeedBackDocument
import com.proper.enterprise.platform.feedback.document.UserFeedBackDocument
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.jdbc.Sql

@Sql("/com/proper/enterprise/platform/feedback/controller/AdminFeedbackControllerTest.sql")
class UserFeedbackServiceImplTest extends AbstractTest {

    @Autowired
    private UserFeedbackService userFeedbackService

    @Before
    void saveTest() {
        UserFeedBackDocument document = new UserFeedBackDocument()
        document.setUserName("Test")
        document.setUserTel("13312341234")
        document.setStatusCode("-1")
        document.setUserId("admin")

        FeedBackDocument feedBackDocument = new FeedBackDocument()
        feedBackDocument.setOpinion("意见内容")
        feedBackDocument.setFeedback("反馈内容")

        List list = new ArrayList()
        list.add(feedBackDocument)
        document.setFeedBackDocuments(list)

        userFeedbackService.save(document)
    }

    @Test
    void findByConditionAndPageTest() {
        PageRequest pageRequest = new PageRequest(0, 10)
        def dataTrunk = userFeedbackService.findByConditionAndPage("-1", "13312341234", pageRequest)
        assert dataTrunk.count == 2
    }

    @Test
    void getUserOpinionsTest() {
        UserFeedBackDocument userFeedBackDocument = userFeedbackService.getUserOpinions("admin")
        assert userFeedBackDocument.getUserName() == "Test"
    }

    @Test
    void saveCloseFeedbackTest() {
        UserFeedBackDocument document = new UserFeedBackDocument()
        document.setUserName("username")
        userFeedbackService.saveCloseFeedback(document)
    }

    @Test
    void saveFeedbackTest() {
        mockUser("admin", "ZL", "123456")
        userFeedbackService.saveFeedback("意见内容", "上传图片的id", "手机型号", "网络环境", "手机系统", "app的版本号")
        mockUser("admin2", "ZL2", "123456")
        userFeedbackService.saveFeedback("意见内容", "上传图片的id", "手机型号", "网络环境", "手机系统", "app的版本号")
    }

    @Test
    void addAdminReplyFeedbackOpinion() {
        UserFeedBackDocument document = new UserFeedBackDocument()
        FeedBackDocument feedBackDocument = new FeedBackDocument()
        feedBackDocument.setOpinion("意见内容")
        List list = new ArrayList()
        list.add(feedBackDocument)
        document.setFeedBackDocuments(list)
        document.setUserName("username")
        userFeedbackService.addAdminReplyFeedbackOpinion("反馈内容", document)
    }

    @Test
    void getFeedbackListTest() {
        mockUser("admin", "ZL", "123456")
        def feedbackList = userFeedbackService.getFeedbackList()
        assert feedbackList.size() == 2
    }

    @Test
    void pushInfoTest() {
        Map map = new HashMap()
        map.put("url", "https://www.baidu.com")
        map.put("title", "test")
        List list = new ArrayList()
        list.add(map)
        try {
            userFeedbackService.pushInfo("推送消息内容", "examList", "admin", list)
        } catch (Exception e) {
        }
    }


}
