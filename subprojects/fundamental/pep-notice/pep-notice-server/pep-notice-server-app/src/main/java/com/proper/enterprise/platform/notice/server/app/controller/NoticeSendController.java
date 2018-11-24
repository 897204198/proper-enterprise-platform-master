package com.proper.enterprise.platform.notice.server.app.controller;

import com.proper.enterprise.platform.api.auth.service.AccessTokenService;
import com.proper.enterprise.platform.core.controller.BaseController;

import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.server.api.exception.NoticeException;
import com.proper.enterprise.platform.notice.server.api.model.Notice;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;
import com.proper.enterprise.platform.notice.server.sdk.request.NoticeRequest;
import com.proper.enterprise.platform.notice.server.api.sender.NoticeSender;
import com.proper.enterprise.platform.notice.server.sdk.request.NoticeTarget;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "/notice/server/send")
@RequestMapping(path = "/notice/server/send")
public class NoticeSendController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoticeSendController.class);

    private NoticeSender noticeSendService;

    private AccessTokenService accessTokenService;

    @Autowired
    public NoticeSendController(NoticeSender noticeSendService, @Qualifier("accessTokenService") AccessTokenService accessTokenService) {
        this.noticeSendService = noticeSendService;
        this.accessTokenService = accessTokenService;
    }

    @PostMapping
    @ApiOperation("‍消息发送")
    public ResponseEntity sendNotice(@ApiParam(value = "‍token") @RequestParam(required = false, name = "access_token") String accessToken,
                                     @RequestBody NoticeRequestVO noticeRequestVO,
                                     HttpServletRequest request) throws NoticeException {
        NoticeRequest noticeRequest = new NoticeRequest();
        BeanUtils.copyProperties(noticeRequestVO, noticeRequest);
        String accessTokenHeader = request.getHeader(AccessTokenService.TOKEN_FLAG_HEADER);
        String token = StringUtil.isEmpty(accessTokenHeader) ? accessToken : accessTokenHeader;
        LOGGER.info("Receive client messages batchId:{}", noticeRequest.getBatchId());
        try {
            List<Notice> notices = noticeSendService
                .beforeSend(accessTokenService.getUserId(token).get(), noticeRequest);
            LOGGER.info("client messages check batchId:{}", noticeRequest.getBatchId());
            noticeSendService.sendAsync(notices);
            return responseOfPost(null);
        } catch (Exception e) {
            LOGGER.error("client send error batchId:{}", noticeRequest.getBatchId(), e);
            throw e;
        }
    }


    public static class NoticeRequestVO {
        @ApiModelProperty(name = "‍批次号,用于标记同一批次发送的消息", required = true)
        private String batchId;

        @ApiModelProperty(name = "‍消息标题", required = true)
        private String title;

        @ApiModelProperty(name = "‍消息内容", required = true)
        private String content;

        @ApiModelProperty(name = "‍发送目标集合", required = true)
        private List<NoticeTarget> targets;

        @ApiModelProperty(name = "‍消息发送类型", required = true)
        private NoticeType noticeType;

        @ApiModelProperty(name = "‍消息扩展信息", required = true)
        private Map<String, Object> noticeExtMsg;


        public String getBatchId() {
            return batchId;
        }

        public void setBatchId(String batchId) {
            this.batchId = batchId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public List<NoticeTarget> getTargets() {
            return targets;
        }

        public void setTargets(List<NoticeTarget> targets) {
            this.targets = targets;
        }

        public NoticeType getNoticeType() {
            return noticeType;
        }

        public void setNoticeType(NoticeType noticeType) {
            this.noticeType = noticeType;
        }

        public Map<String, Object> getNoticeExtMsg() {
            return noticeExtMsg;
        }

        public void setNoticeExtMsg(Map<String, Object> noticeExtMsg) {
            this.noticeExtMsg = noticeExtMsg;
        }

        @Override
        public String toString() {
            return JSONUtil.toJSONIgnoreException(this);
        }
    }

}
