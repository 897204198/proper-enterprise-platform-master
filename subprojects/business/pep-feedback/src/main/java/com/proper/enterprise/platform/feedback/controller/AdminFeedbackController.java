package com.proper.enterprise.platform.feedback.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.feedback.document.FeedBackDocument;
import com.proper.enterprise.platform.feedback.document.UserFeedBackDocument;
import com.proper.enterprise.platform.feedback.service.UserFeedbackService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "/admin/feedback")
@RequestMapping(value = "/admin/feedback")
public class AdminFeedbackController extends BaseController {

    private UserFeedbackService feedbackService;

    @Autowired
    public AdminFeedbackController(UserFeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping
    @ApiOperation("‍管理端取得App所有用户意见反馈的集合")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "pageNo", value = "‍页码", required = true, paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "pageSize", value = "‍每页条数", required = true, paramType = "query", dataType = "int")
    })
    public ResponseEntity<DataTrunk<UserFeedBackDocument>> getUserOpinionFeedBackList(
            @ApiParam("‍反馈状态，可选值：-1, 0, 1，含义需补充") @RequestParam(required = false) String feedbackStatus,
            @ApiParam("‍姓名或手机号模糊匹配") @RequestParam(required = false) String query) {
        return responseOfGet(feedbackService.findByConditionAndPage(feedbackStatus, query, getPageRequest(Sort.Direction.DESC, "createTime")));
    }

    @GetMapping(path = "/{userId}")
    @ApiOperation("‍取得管理端用户意见反馈列表")
    public ResponseEntity<List<FeedBackDocument>> getUserOpinionFeedBack(
            @ApiParam(value = "‍用户ID", required = true) @PathVariable String userId) {
        UserFeedBackDocument userFeedBackDocument = feedbackService.getUserOpinions(userId);
        return responseOfGet(userFeedBackDocument.getFeedBackDocuments());
    }

    @PostMapping(path = "/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("‍根据用户Id保存管理端反馈意见")
    public ResponseEntity<UserFeedBackDocument> addOpinionFeedback(
            @ApiParam(value = "‍用户ID", required = true) @PathVariable String userId,
            @RequestBody AdminFeedbackVO param) throws Exception {
        String feedback = param.getFeedback();
        UserFeedBackDocument feedBackDocument = feedbackService.getUserOpinions(userId);
        return responseOfPost(feedbackService.addAdminReplyFeedbackOpinion(feedback, feedBackDocument));
    }

    @PutMapping(path = "/{userId}/close")
    @ApiOperation("‍关闭状态更新管理端的意见反馈")
    public ResponseEntity<String> updateCloseOpinionFeedback(
            @ApiParam(value = "‍用户ID", required = true) @PathVariable String userId) {
        UserFeedBackDocument userFeedBackDocument = feedbackService.getUserOpinions(userId);
        return responseOfPut(feedbackService.saveCloseFeedback(userFeedBackDocument));
    }

    public static class AdminFeedbackVO {

        @ApiModelProperty(name = "‍反馈内容", required = true)
        private String feedback;

        public String getFeedback() {
            return feedback;
        }

        public void setFeedback(String feedback) {
            this.feedback = feedback;
        }

        @Override
        public String toString() {
            return JSONUtil.toJSONIgnoreException(this);
        }

    }

}
