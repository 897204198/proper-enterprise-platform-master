package com.proper.enterprise.platform.notice.server.email.constant;

public class PEPMailConstant {

    /**
     * 如果设置为 true, 则开启 authenticate 认证, 需要设置邮箱服务的账户和密码
     */
    public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";

    /**
     * 创建 smtp sockets. {@link javax.net.SocketFactory}
     */
    public static final String MAIL_SMTP_SOCKET_FACTORY_CLASS = "mail.smtp.socketFactory.class";

    /**
     * 使用TLS连接
     */
    public static final String MAIL_TRANSPORT_STARTTLS_ENABLE = "mail.smtp.starttls.enable";

    /**
     * 邮件默认发送地址
     */
    public static final String MAIL_FROM = "mail.from";
}
