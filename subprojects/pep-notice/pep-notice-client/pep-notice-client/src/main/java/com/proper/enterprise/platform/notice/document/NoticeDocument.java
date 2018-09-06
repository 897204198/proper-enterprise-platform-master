package com.proper.enterprise.platform.notice.document;

import com.proper.enterprise.platform.core.mongo.document.BaseDocument;
import com.proper.enterprise.platform.notice.entity.NoticeSetDocument;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;
import com.proper.enterprise.platform.notice.server.sdk.request.NoticeTarget;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Document(collection = "PEP_NOTICE_MSG")
public class NoticeDocument extends BaseDocument {

    public NoticeDocument() {
    }

    @Indexed
    private String batchId;

    private String title;

    private String content;

    private Set<String> users;

    private List<NoticeTarget> targets;

    private NoticeType noticeType;

    private Map<String, Object> noticeExtMsg;

    private NoticeSetDocument noticeSetDocument;

    private String exception;

    private String note;

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

    public void setTarget(NoticeTarget target) {
        if (targets == null) {
            targets = new ArrayList<>();
        }
        targets.add(target);
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

    public void setNoticeExtMsg(String key, Object value) {
        if (noticeExtMsg == null) {
            noticeExtMsg = new HashMap<>(1);
        }
        noticeExtMsg.put(key, value);
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Set<String> getUsers() {
        return users;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }

    public NoticeSetDocument getNoticeSetDocument() {
        return noticeSetDocument;
    }

    public void setNoticeSetDocument(NoticeSetDocument noticeSetDocument) {
        this.noticeSetDocument = noticeSetDocument;
    }
}
