package com.proper.enterprise.platform.feedback.controller

import com.proper.enterprise.platform.auth.common.jpa.repository.UserRepository
import com.proper.enterprise.platform.feedback.document.FeedBackDocument
import com.proper.enterprise.platform.feedback.document.UserFeedBackDocument
import com.proper.enterprise.platform.feedback.repository.UserFeedBackRepository
import com.proper.enterprise.platform.sys.datadic.DataDic
import com.proper.enterprise.platform.sys.datadic.service.DataDicService
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.After
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

@Sql
class AdminFeedbackControllerTest extends AbstractTest {

    @Autowired
    UserFeedBackRepository userFeedBackRepo

    @Autowired
    UserRepository userRepository

    @Autowired
    DataDicService dataDicService

    @Test
    void testAdminGetAndPostFeedback() {
        mockUser('id1', 'name1', 'pwd')
        // admin post && get && post
        UserFeedBackDocument userFeedBackDocument = new UserFeedBackDocument()
        userFeedBackDocument.setUserId("id1")
        userFeedBackDocument.setUserName("name1")
        userFeedBackDocument.setUserTel("12345678901")
        FeedBackDocument document = new FeedBackDocument()
        document.setFeedback("reply a data !")
        document.setMobileModel("iphone")
        document.setNetType("4G")
        document.setAppVersion("v4.0.2")
        document.setPlatform("IOS")
        List<FeedBackDocument> arrayList = new ArrayList<>()
        arrayList.add(document)
        userFeedBackDocument.setFeedBackDocuments(arrayList)
        userFeedBackDocument = userFeedBackRepo.save(userFeedBackDocument)
        post('/admin/feedback/' + userFeedBackDocument.getUserId(), JSONUtil.toJSON(userFeedBackDocument), HttpStatus.OK)

        def list = resOfGet("/admin/feedback/" + "/" + userFeedBackDocument.getUserId(), HttpStatus.OK)
        assert list.size() == 2
        assert list.get(0).get("feedback") == "reply a data !"
        assert list.get(0).get("mobileModel") == "iphone"
        assert list.get(0).get("netType") == "4G"
        assert list.get(0).get("appVersion") == "v4.0.2"
        assert list.get(0).get("platform") == "IOS"
    }

    @Test
    void testAdminPutFeedback() {
        mockUser('id', 'name', 'pwd')

        UserFeedBackDocument userFeedBackDocument = new UserFeedBackDocument()
        userFeedBackDocument.setUserId("id")
        userFeedBackDocument.setUserName("name")
        userFeedBackDocument.setUserTel("12345678901")
        DataDic unReplyDic = dataDicService.get("pep_feedback_not_reply")
        userFeedBackDocument.setStatus(unReplyDic.getName())
        userFeedBackDocument.setStatusCode(unReplyDic.getCode())
        userFeedBackDocument = userFeedBackRepo.save(userFeedBackDocument)

        put('/admin/feedback/' + userFeedBackDocument.getUserId() + '/close', JSONUtil.toJSON(userFeedBackDocument), HttpStatus.OK)

    }

    @Test
    void testGetFeedbackList() {
        userFeedBackRepo.deleteAll()
        userRepository.deleteAll()

        mockUser('idd1', 't1', 'pwd')
        UserFeedBackDocument userFeedBackDocument = new UserFeedBackDocument()
        userFeedBackDocument.setUserId("id1")
        userFeedBackDocument.setUserName("name1")
        userFeedBackDocument.setUserTel("12345678901")
        userFeedBackDocument.setStatusCode("1")
        FeedBackDocument feedBackDocument = new FeedBackDocument()
        feedBackDocument.setOpinion("用户提交的opinion1")
        FeedBackDocument feedBackDocument1 = new FeedBackDocument()
        feedBackDocument1.setFeedback("admin反馈的feedback2")
        feedBackDocument1.setMobileModel("iphone")
        feedBackDocument1.setNetType("4G")
        feedBackDocument1.setAppVersion("v4.0.2")
        feedBackDocument1.setPlatform("IOS")
        List<FeedBackDocument> feedBackDocumentArrayList = new  ArrayList<>()
        feedBackDocumentArrayList.add(feedBackDocument)
        feedBackDocumentArrayList.add(feedBackDocument1)
        userFeedBackDocument.setFeedBackDocuments(feedBackDocumentArrayList)
        userFeedBackRepo.save(userFeedBackDocument)

        mockUser('idd2', 't2', 'pwd')
        UserFeedBackDocument userFeedBackDocument1 = new UserFeedBackDocument()
        userFeedBackDocument1.setUserId("id2")
        userFeedBackDocument1.setUserName("name2")
        userFeedBackDocument1.setUserTel("12345678902")
        userFeedBackDocument1.setStatusCode("0")
        userFeedBackRepo.save(userFeedBackDocument1)

        mockUser('idd3', 't3', 'pwd')
        UserFeedBackDocument userFeedBackDocument2 = new UserFeedBackDocument()
        userFeedBackDocument2.setUserId("id3")
        userFeedBackDocument2.setUserName("name3")
        userFeedBackDocument2.setUserTel("12345678903")
        userFeedBackDocument2.setStatusCode("0")
        FeedBackDocument feedBackDocuments = new FeedBackDocument()
        feedBackDocuments.setPictureId("img.pic")
        List<FeedBackDocument> documentArrayList = new  ArrayList<>()
        documentArrayList.add(feedBackDocuments)
        userFeedBackDocument2.setFeedBackDocuments(documentArrayList)
        userFeedBackRepo.save(userFeedBackDocument2)

        Map<String, Object> doc4 = resOfGet('/admin/feedback?query=&feedbackStatus=-1&pageNo=1&pageSize=3', HttpStatus.OK)
        int count4 = Integer.parseInt(String.valueOf(doc4.get("count")))
        List<Map<String, String>> retList4 = (List<Map<String, Object>>) doc4.get("data")
        assert count4 == 3
        String value4 = retList4.get(0).get("userName")
        assert value4 == "name3"

        Map<String, Object> doc1 = resOfGet('/admin/feedback?query=&feedbackStatus=0&pageNo=1&pageSize=3', HttpStatus.OK)
        int count1 = Integer.parseInt(String.valueOf(doc1.get("count")))
        List<Map<String, String>> retList1 = (List<Map<String, Object>>) doc1.get("data")
        assert count1 == 2
        assert retList1.size() == 2
        String value1 = retList1.get(0).get("userName")
        assert value1 == "name3"


        Map<String, Object> doc2 = resOfGet('/admin/feedback?query=name&feedbackStatus=1&pageNo=1&pageSize=2', HttpStatus.OK)
        int count2 = Integer.parseInt(String.valueOf(doc2.get("count")))
        List<Map<String, String>> retList2 = (List<Map<String, Object>>) doc2.get("data")
        assert count2 == 1
        String value2 = retList2.get(0).get("userName")
        assert value2 == "name1"
        def feedbackDocs =retList2.get(0).get("feedBackDocuments")
        assert feedbackDocs.size == 1
        assert feedbackDocs[0].feedback == 'admin反馈的feedback2'
        assert feedbackDocs[0].mobileModel == 'iphone'
        assert feedbackDocs[0].netType == '4G'
        assert feedbackDocs[0].appVersion == 'v4.0.2'
        assert feedbackDocs[0].platform == 'IOS'


        Map<String, Object> doc3 = resOfGet('/admin/feedback?query=&feedbackStatus=0&pageNo=1&pageSize=1', HttpStatus.OK)
        int count3 = Integer.parseInt(String.valueOf(doc3.get("count")))
        List<Map<String, String>> retLists = (List<Map<String, Object>>) doc3.get("data")
        assert count3 == 2
        String value3 = retLists.get(0).get("userName")
        assert value3 == "name3"
        def feedbackDoc =retLists.get(0).get("feedBackDocuments")
        assert feedbackDoc.size == 1
        assert feedbackDoc[0].pictureId == 'img.pic'

        Map<String, Object> doc5 = resOfGet('/admin/feedback?query=me2&feedbackStatus=0&pageNo=1&pageSize=2', HttpStatus.OK)
        int count5 = Integer.parseInt(String.valueOf(doc5.get("count")))
        List<Map<String, String>> retList5 = (List<Map<String, Object>>) doc5.get("data")
        assert count5 == 1
        String value5 = retList5.get(0).get("userName")
        assert value5 == "name2"
    }

    @After
    void clean() {
        userFeedBackRepo.deleteAll()
        userRepository.deleteAll()
    }
}
