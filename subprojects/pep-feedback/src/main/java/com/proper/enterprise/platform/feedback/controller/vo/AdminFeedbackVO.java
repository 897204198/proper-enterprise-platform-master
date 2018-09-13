package com.proper.enterprise.platform.feedback.controller.vo;

import com.proper.enterprise.platform.core.utils.JSONUtil;
import io.swagger.annotations.ApiModelProperty;

public class AdminFeedbackVO {

    @ApiModelProperty("‍反馈内容")
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
