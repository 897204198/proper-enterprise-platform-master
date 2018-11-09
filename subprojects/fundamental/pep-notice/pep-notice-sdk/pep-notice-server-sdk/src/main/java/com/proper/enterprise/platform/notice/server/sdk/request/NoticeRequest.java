package com.proper.enterprise.platform.notice.server.sdk.request;

import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;

import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoticeRequest implements Serializable {

    public interface NoticeSendApi {

    }

    /**
     * 批次号  用于标记同一批次发送的消息
     */
    @NotEmpty(message = "{notice.server.param.batchId.cantBeEmpty}", groups = NoticeSendApi.class)
    @Length(message = "{notice.server.param.batchId.isTooLong}", max = 36, groups = NoticeSendApi.class)
    private String batchId;

    /**
     * 消息标题
     */
    @Length(message = "{notice.server.param.title.isTooLong}", max = 36, groups = NoticeSendApi.class)
    private String title;

    /**
     * 消息内容
     */
    @NotEmpty(message = "{notice.server.param.content.cantBeEmpty}", groups = NoticeSendApi.class)
    @Length(message = "{notice.server.param.content.isTooLong}", max = 2048, groups = NoticeSendApi.class)
    private String content;

    /**
     * 发送目标集合
     */
    @Valid
    @NotEmpty(message = "{notice.server.param.target.cantBeEmpty}", groups = NoticeSendApi.class)
    private List<NoticeTarget> targets;

    /**
     * 消息发送类型
     */
    @NotNull(message = "{notice.server.param.noticeType.cantBeEmpty}", groups = NoticeSendApi.class)
    private NoticeType noticeType;

    /**
     * 消息扩展信息
     */
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

    public void setNoticeExtMsg(String key, Object msg) {
        if (StringUtil.isEmpty(key)) {
            return;
        }
        if (null == this.noticeExtMsg) {
            noticeExtMsg = new HashMap<>(16);
        }
        noticeExtMsg.put(key, msg);
    }

    public void addTarget(NoticeTarget target) {
        if (CollectionUtil.isEmpty(this.targets)) {
            this.targets = new ArrayList<>();
        }
        this.targets.add(target);
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }

}
