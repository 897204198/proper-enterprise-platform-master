package com.proper.enterprise.platform.notice.server.sms.handler;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.i18n.I18NService;
import com.proper.enterprise.platform.core.i18n.I18NUtil;
import com.proper.enterprise.platform.core.utils.http.Callback;
import com.proper.enterprise.platform.core.utils.http.HttpClient;
import com.proper.enterprise.platform.notice.server.api.handler.NoticeSendHandler;
import com.proper.enterprise.platform.notice.server.api.model.BusinessNotice;
import com.proper.enterprise.platform.notice.server.api.model.BusinessNoticeResult;
import com.proper.enterprise.platform.notice.server.api.model.ReadOnlyNotice;
import com.proper.enterprise.platform.notice.server.api.util.ThrowableMessageUtil;
import com.proper.enterprise.platform.notice.server.sdk.constants.NoticeErrorCodeConstants;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;
import com.proper.enterprise.platform.notice.server.sms.configurator.SMSConfigurator;
import com.proper.enterprise.platform.notice.server.sms.service.SMSLimitCheckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.Map;

@Service("smsNoticeSender")
public class SMSNoticeSendHandler implements NoticeSendHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SMSNoticeSendHandler.class);

    private static final String SUCCESS = "Status=Succ";

    @Autowired
    private SMSLimitCheckService smsLimitCheckService;

    @Autowired
    private SMSConfigurator noticeConfigurator;

    @Autowired
    private I18NService i18NService;

    /**
     * 异步调用发送手机短信接口
     * 不等待短信接口请求返回
     * 若在拼接请求发送的 data 时发生异常，则抛出ErrMsgException
     *
     * @param notice 消息对象
     */
    @Override
    public BusinessNoticeResult send(ReadOnlyNotice notice) {
        Map smsNoticeConfigurator = noticeConfigurator.get(notice.getAppKey());
        String phone = notice.getTargetTo();
        String message = notice.getContent();
        String charset = (String) smsNoticeConfigurator.get("smsCharset");
        String data = "";
        try {
            data = MessageFormat.format((String) smsNoticeConfigurator.get("smsTemplate"), phone,
                URLEncoder.encode(message, charset));
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Exception occurs when composing POST data: phone({}), message({})", phone, message, e);
            return new BusinessNoticeResult(NoticeStatus.FAIL, e.getMessage(), I18NUtil.getMessage("pep.notice.sms.send.error"));
        }
        String url = (String) smsNoticeConfigurator.get("smsUrl");
        String finalData = data;
        try {
            HttpClient.post(url, MediaType.APPLICATION_FORM_URLENCODED, data, new Callback() {
                @Override
                public void onSuccess(ResponseEntity<byte[]> responseEntity) {
                    String resBody = "";
                    byte[] body = responseEntity.getBody();
                    if (body != null) {
                        resBody = new String(body, Charset.forName(charset));
                    }
                    if (!resBody.contains(SUCCESS)) {
                        LOGGER.error("Send sms (POST: {}, data: {}) FAILED! Status code: {}, Response body: {}",
                            url, finalData, responseEntity.getStatusCode(), resBody);
                        throw new ErrMsgException(i18NService.getMessage("pep.notice.sms.send.error"));
                    }
                }

                @Override
                public void onError(IOException ioe) {
                    LOGGER.error("Exception occurs when sending SMS: phone({}), message({})", phone, message, ioe);
                    throw new ErrMsgException(i18NService.getMessage("pep.notice.sms.send.error"));
                }
            });
        } catch (Exception e) {
            return new BusinessNoticeResult(NoticeStatus.FAIL, e.getMessage(), ThrowableMessageUtil.getStackTrace(e));
        }
        return new BusinessNoticeResult(NoticeStatus.SUCCESS);
    }

    @Override
    public BusinessNoticeResult beforeSend(BusinessNotice notice) {
        boolean result = smsLimitCheckService.couldSendSMS(notice.getTargetTo());
        if (!result) {
            return new BusinessNoticeResult(NoticeStatus.FAIL, NoticeErrorCodeConstants.CHECK_ERROR,
                I18NUtil.getMessage("pep.notice.sms.get.frequently.try.again"));
        }
        return new BusinessNoticeResult(NoticeStatus.SUCCESS);
    }

    @Override
    public void afterSend(ReadOnlyNotice notice) {

    }

    @Override
    public BusinessNoticeResult getStatus(ReadOnlyNotice notice) {
        return new BusinessNoticeResult(NoticeStatus.SUCCESS);
    }
}
