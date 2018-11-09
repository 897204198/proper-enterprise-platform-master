package com.proper.enterprise.platform.notice.document;

import com.proper.enterprise.platform.core.mongo.document.BaseDocument;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Document(collection = "PEP_NOTICE_SET")
public class NoticeSetDocument extends BaseDocument {

    public NoticeSetDocument() {
    }

    public NoticeSetDocument(String... noticeChannel) {
        this.noticeChannel = new ArrayList<String>(Arrays.asList(noticeChannel));
    }

    /**
     *  用户ID
     */
    private String userId;

    /**
     *  目录
     */
    private String catalog;

    private String name;

    private List<String> noticeChannel;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getNoticeChannel() {
        return noticeChannel;
    }

    public void setNoticeChannel(List<String> noticeChannel) {
        this.noticeChannel = noticeChannel;
    }
}
