package com.proper.enterprise.platform.feedback.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.feedback.document.FeedBackDocument;
import com.proper.enterprise.platform.feedback.document.UserFeedBackDocument;
import com.proper.enterprise.platform.feedback.service.UserFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/admin/feedback")
public class AdminFeedbackController extends BaseController {

    private UserFeedbackService feedbackService;

    @Autowired
    public AdminFeedbackController(UserFeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    /**
     * 管理端取得App所有用户的意见反馈
     *
     * @param feedbackStatus 反馈状态
     * @param query          查询条件
     * @return 返回意见反馈的分页数据
     */
    @GetMapping
    public ResponseEntity<DataTrunk<UserFeedBackDocument>> getUserOpinionFeedBackList(String feedbackStatus, String query) {
        return responseOfGet(feedbackService.findByConditionAndPage(feedbackStatus, query, getPageRequest(Sort.Direction.DESC, "createTime")));
    }

    /**
     * 取得管理端的用户意见反馈列表
     *
     * @param userId 非空
     * @return 返回用户的意见反馈的列表
     */
    @GetMapping(path = "/{userId}")
    public ResponseEntity<List<FeedBackDocument>> getUserOpinionFeedBack(@PathVariable String userId) {
        UserFeedBackDocument userFeedBackDocument = feedbackService.getUserOpinions(userId);
        return responseOfGet(userFeedBackDocument.getFeedBackDocuments());
    }

    /**
     * 根据用户Id保存管理端反馈意见
     *
     * @param param 非空
     * @return 返回给调用方的应答.
     */
    @PostMapping(path = "/{userId}")
    public UserFeedBackDocument addOpinionFeedback(@PathVariable String userId, @RequestBody Map<String, Object> param) throws Exception {
        String feedback = (String) param.get("feedback");
        UserFeedBackDocument feedBackDocument = feedbackService.getUserOpinions(userId);
        return feedbackService.addAdminReplyFeedbackOpinion(feedback, feedBackDocument);
    }

    /**
     * 关闭状态更新管理端的意见反馈
     *
     * @param userId 非空
     * @return 返回给调用方的应答.
     */
    @PutMapping(path = "/{userId}/close")
    public ResponseEntity<String> updateCloseOpinionFeedback(@PathVariable String userId) {
        UserFeedBackDocument userFeedBackDocument = feedbackService.getUserOpinions(userId);
        return responseOfPut(feedbackService.saveCloseFeedback(userFeedBackDocument));
    }

}
