package com.proper.enterprise.platform.notice.document;

import com.proper.enterprise.platform.core.mongo.document.BaseDocument;
import com.proper.enterprise.platform.notice.enums.AnalysisResult;
import com.proper.enterprise.platform.notice.model.TargetModel;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Document(collection = "PEP_NOTICE_MSG")
public class NoticeDocument extends BaseDocument {

    public NoticeDocument() {
    }

    @Indexed(unique = true)
    private String batchId;

    private String title;

    private String content;

    private Set<String> users;

    private List<TargetModel> targets;

    private NoticeType noticeType;

    private Map<String, Object> noticeExtMsg;

    private String exception;

    private List<String> notes;

    private AnalysisResult analysisResult;

    private String noticeUrl;

    private String token;

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

    public List<TargetModel> getTargets() {
        return targets;
    }

    public void setTargets(List<TargetModel> targets) {
        this.targets = targets;
    }

    public void setTarget(TargetModel target) {
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

    public List<String> getNotes() {
        return notes;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }

    public void setNotes(String note) {
        if (notes == null) {
            notes = new ArrayList<>();
        }
        notes.add(note);
    }

    public void setNotes(String note, Object... args) {
        if (notes == null) {
            notes = new ArrayList<>();
        }
        notes.add(String.format(note, args));
    }

    public Set<String> getUsers() {
        return users;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }

    public AnalysisResult getAnalysisResult() {
        return analysisResult;
    }

    public void setAnalysisResult(AnalysisResult analysisResult) {
        this.analysisResult = analysisResult;
    }

    public String getNoticeUrl() {
        return noticeUrl;
    }

    public void setNoticeUrl(String noticeUrl) {
        this.noticeUrl = noticeUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
