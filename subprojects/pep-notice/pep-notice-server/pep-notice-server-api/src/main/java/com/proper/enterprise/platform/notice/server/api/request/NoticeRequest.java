package com.proper.enterprise.platform.notice.server.api.request;

import com.proper.enterprise.platform.core.PEPApplicationContext;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.notice.server.api.enums.NoticeType;
import com.proper.enterprise.platform.notice.server.api.model.NoticeOperation;
import com.proper.enterprise.platform.notice.server.api.service.NoticeDaoService;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoticeRequest {

    /**
     * 批次号  用于标记同一批次发送的消息
     */
    @NotEmpty(message = "notice.server.param.batchId.cantBeEmpty")
    @Length(message = "notice.server.param.batchId.isTooLong", max = 36)
    private String batchId;

    /**
     * 消息标题
     */
    @Length(message = "notice.server.param.title.isTooLong", max = 36)
    private String title;

    /**
     * 消息内容
     */
    @NotEmpty(message = "notice.server.param.content.cantBeEmpty")
    @Length(message = "notice.server.param.content.isTooLong", max = 2048)
    private String content;

    /**
     * 发送目标集合
     */
    @NotEmpty(message = "notice.server.param.target.cantBeEmpty")
    private List<NoticeTarget> targets;

    /**
     * 消息发送类型
     */
    @NotNull(message = "notice.server.param.noticeType.cantBeEmpty")
    private NoticeType noticeType;

    /**
     * 消息扩展信息
     */
    @Length(message = "notice.server.param.noticeExtMsg.isTooLong", max = 2048)
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

    public void addTarget(NoticeTarget targetModel) {
        if (targets == null) {
            targets = new ArrayList<>();
        }
        targets.add(targetModel);
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

    public void addNoticeExtMsg(String key, String value) {
        if (noticeExtMsg == null) {
            noticeExtMsg = new HashMap<>(1);
        }
        noticeExtMsg.put(key, value);
    }

    public List<NoticeOperation> convert() {
        List<NoticeOperation> noticeOperations = new ArrayList<>();
        for (NoticeTarget target : this.targets) {
            NoticeOperation noticeOperation = PEPApplicationContext.getBean(NoticeDaoService.class).newNotice();
            noticeOperation.setBatchId(this.batchId);
            noticeOperation.setTitle(this.title);
            noticeOperation.setContent(this.content);
            noticeOperation.setTargetTo(target.getTo());
            noticeOperation.setAllTargetExtMsg(target.getTargetExtMsg());
            noticeOperation.setAllNoticeExtMsg(this.noticeExtMsg);
            noticeOperations.add(noticeOperation);
        }
        return noticeOperations;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }

}
