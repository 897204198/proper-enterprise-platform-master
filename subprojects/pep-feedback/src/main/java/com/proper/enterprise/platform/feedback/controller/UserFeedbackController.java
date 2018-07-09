package com.proper.enterprise.platform.feedback.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.feedback.document.FeedBackDocument;
import com.proper.enterprise.platform.feedback.service.UserFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 意见反馈.
 */
@RestController
@RequestMapping(value = "/user/feedback")
public class UserFeedbackController extends BaseController {

    private UserFeedbackService feedbackService;

    @Autowired
    public UserFeedbackController(UserFeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    /**
     * 保存APP用户意见.
     *
     * @param param 用户意见信息.
     * @return 返回给调用方的应答.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> saveFeedbackInfo(@RequestBody Map<String, Object> param) throws Exception {
        String opinion = (String) param.get("opinion");
        String pictureId = (String) param.get("pictureId");
        String mobileModel = (String) param.get("mobileModel");
        String netType = (String) param.get("netType");
        String platform = (String) param.get("platform");
        String appVersion = (String) param.get("appVersion");
        return responseOfPost(feedbackService.saveFeedback(opinion, pictureId, mobileModel, netType, platform, appVersion));
    }

    /**
     * 取得APP用户意见列表.
     *
     * @return 返回给调用方的应答.
     * @throws Exception 异常.
     */
    @GetMapping
    public ResponseEntity<List<FeedBackDocument>> getUserOpinion() throws Exception {
        return responseOfGet(feedbackService.getFeedbackList());
    }
}
