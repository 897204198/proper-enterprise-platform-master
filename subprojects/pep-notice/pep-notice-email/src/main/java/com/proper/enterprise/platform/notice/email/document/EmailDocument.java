package com.proper.enterprise.platform.notice.email.document;

import com.proper.enterprise.platform.core.mongo.document.BaseDocument;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "PEP_NOTICE_EMAIL")
public class EmailDocument extends BaseDocument {

    public EmailDocument(){
    }

    /**
     * 唯一标识
     */
    @Indexed(unique = true)
    private String appKey;

    /**
     * 邮箱服务端Host
     */
    private String mailServerHost;

    /**
     * 邮箱服务端用户名
     */
    private String mailServerUsername;

    /**
     * 邮箱服务端密码(一般为客户端密钥)
     */
    private String mailServerPassword;

    /**
     * 邮箱服务端端口
     */
    private Integer mailServerPort;

    /**
     * 邮箱服务使用SSL
     */
    private Boolean mailServerUseSSL;

    /**
     * 邮箱服务使用TLS
     */
    private Boolean mailServerUseTLS;

    /**
     * 邮箱默认发送人
     */
    private String mailServerDefaultFrom;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getMailServerHost() {
        return mailServerHost;
    }

    public void setMailServerHost(String mailServerHost) {
        this.mailServerHost = mailServerHost;
    }

    public String getMailServerUsername() {
        return mailServerUsername;
    }

    public void setMailServerUsername(String mailServerUsername) {
        this.mailServerUsername = mailServerUsername;
    }

    public String getMailServerPassword() {
        return mailServerPassword;
    }

    public void setMailServerPassword(String mailServerPassword) {
        this.mailServerPassword = mailServerPassword;
    }

    public Integer getMailServerPort() {
        return mailServerPort;
    }

    public void setMailServerPort(Integer mailServerPort) {
        this.mailServerPort = mailServerPort;
    }

    public Boolean getMailServerUseSSL() {
        return mailServerUseSSL;
    }

    public void setMailServerUseSSL(Boolean mailServerUseSSL) {
        this.mailServerUseSSL = mailServerUseSSL;
    }

    public Boolean getMailServerUseTLS() {
        return mailServerUseTLS;
    }

    public void setMailServerUseTLS(Boolean mailServerUseTLS) {
        this.mailServerUseTLS = mailServerUseTLS;
    }

    public String getMailServerDefaultFrom() {
        return mailServerDefaultFrom;
    }

    public void setMailServerDefaultFrom(String mailServerDefaultFrom) {
        this.mailServerDefaultFrom = mailServerDefaultFrom;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
