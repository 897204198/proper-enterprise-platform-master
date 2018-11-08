package com.proper.enterprise.platform.feedback.controller

import com.proper.enterprise.platform.auth.common.jpa.entity.UserEntity
import com.proper.enterprise.platform.auth.common.jpa.repository.UserRepository
import com.proper.enterprise.platform.feedback.document.FeedBackDocument
import com.proper.enterprise.platform.feedback.document.UserFeedBackDocument
import com.proper.enterprise.platform.feedback.repository.UserFeedBackRepository
import com.proper.enterprise.platform.sys.datadic.service.DataDicService
import com.proper.enterprise.platform.test.AbstractJPATest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

@Sql
class UserFeedbackControllerTest extends AbstractJPATest {
    @Autowired
    UserFeedBackRepository userFeedBackRepo

    @Autowired
    UserRepository userRepository

    @Autowired
    DataDicService dataDicService

    @Test
    void testAppSaveAndGetFeedbackInfo() {
        mockUser('feedback', 't1', 'pwd')
        // app post && get list
        UserFeedBackDocument userFeedBackDocument = new UserFeedBackDocument()
        userFeedBackDocument.setUserId("feedback")
        userFeedBackDocument.setUserName("t1")
        FeedBackDocument feedBackDocument = new FeedBackDocument()
        feedBackDocument.setOpinion("testOpinion")
        FeedBackDocument feedBackImg = new FeedBackDocument()
        feedBackImg.setPictureId("image.pic")
        List<FeedBackDocument> feedBackDocumentList = new ArrayList<>()
        feedBackDocumentList.add(feedBackDocument)
        feedBackDocumentList.add(feedBackImg)
        userFeedBackDocument.setFeedBackDocuments(feedBackDocumentList)
        userFeedBackDocument = userFeedBackRepo.save(userFeedBackDocument)
        post("/user/feedback", JSONUtil.toJSON(userFeedBackDocument), HttpStatus.CREATED)

        def list = JSONUtil.parse(get("/user/feedback", HttpStatus.OK).getResponse().getContentAsString(), Object.class)
        assert list.get(0).get("opinion") == "testOpinion"
        assert list.get(1).get("pictureId") == "image.pic"
    }

    @Before
    void testSaveUserInfo(){
        UserEntity userInfo = new UserEntity()
        userInfo.setName("feedbackUser")
        userInfo.setPassword("pwd")
        userInfo.setPhone("13900001234")
        userInfo.setId("feedback")
        userInfo.setUsername("feedbackUser")
        userRepository.save(userInfo)
    }

    @After
    void clean() {
        userFeedBackRepo.deleteAll()
        userRepository.deleteAll()
    }
}
