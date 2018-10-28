package com.proper.enterprise.platform.feedback.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.feedback.document.FeedBackDocument;
import com.proper.enterprise.platform.feedback.service.UserFeedbackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "/user/feedback")
@RequestMapping(value = "/user/feedback")
public class UserFeedbackController extends BaseController {

    private UserFeedbackService feedbackService;

    @Autowired
    public UserFeedbackController(UserFeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation("‍保存APP用户意见")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> saveFeedbackInfo(@RequestBody FeedbackVO param) throws Exception {
        String opinion = param.getOpinion();
        String pictureId = param.getPictureId();
        String mobileModel = param.getPictureId();
        String netType = param.getNetType();
        String platform = param.getPlatform();
        String appVersion = param.getAppVersion();
        return responseOfPost(feedbackService.saveFeedback(opinion, pictureId, mobileModel, netType, platform, appVersion));
    }

    @GetMapping
    @ApiOperation("‍取得APP用户意见列表")
    public ResponseEntity<List<FeedBackDocument>> getUserOpinion() throws Exception {
        return responseOfGet(feedbackService.getFeedbackList());
    }

    public static class FeedbackVO {

        @ApiModelProperty(name = "‍意见内容", required = true)
        private String opinion;

        @ApiModelProperty(name = "‍上传图片的id", required = true)
        private String pictureId;

        @ApiModelProperty(name = "‍手机型号", required = true)
        private String mobileModel;

        @ApiModelProperty(name = "‍网络环境", required = true)
        private String netType;

        @ApiModelProperty(name = "‍app的版本号", required = true)
        private String appVersion;

        @ApiModelProperty(name = "‍手机系统", required = true)
        private String platform;

        public String getOpinion() {
            return opinion;
        }

        public void setOpinion(String opinion) {
            this.opinion = opinion;
        }

        public String getPictureId() {
            return pictureId;
        }

        public void setPictureId(String pictureId) {
            this.pictureId = pictureId;
        }

        public String getMobileModel() {
            return mobileModel;
        }

        public void setMobileModel(String mobileModel) {
            this.mobileModel = mobileModel;
        }

        public String getNetType() {
            return netType;
        }

        public void setNetType(String netType) {
            this.netType = netType;
        }

        public String getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
        }

        public String getPlatform() {
            return platform;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }

        @Override
        public String toString() {
            return JSONUtil.toJSONIgnoreException(this);
        }
    }
}
