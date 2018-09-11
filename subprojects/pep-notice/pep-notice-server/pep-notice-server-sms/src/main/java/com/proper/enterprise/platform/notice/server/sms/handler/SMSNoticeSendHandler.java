package com.proper.enterprise.platform.notice.server.sms.handler;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.http.Callback;
import com.proper.enterprise.platform.core.utils.http.HttpClient;
import com.proper.enterprise.platform.notice.server.api.exception.NoticeException;
import com.proper.enterprise.platform.notice.server.api.handler.NoticeSendHandler;
import com.proper.enterprise.platform.notice.server.api.model.BusinessNotice;
import com.proper.enterprise.platform.notice.server.api.model.ReadOnlyNotice;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;
import com.proper.enterprise.platform.notice.server.sms.configurator.SMSConfigurator;
import com.proper.enterprise.platform.notice.server.sms.service.SMSLimitCheckService;
import com.proper.enterprise.platform.sys.i18n.I18NService;
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

    private static final String SUCCESS = "0:";

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
    public void send(ReadOnlyNotice notice) throws NoticeException {
        Map smsNoticeConfigurator = noticeConfigurator.get(notice.getAppKey());
        String phone = notice.getTargetTo();
        String message = notice.getContent();
        String charset = (String) smsNoticeConfigurator.get("smsCharset");
        String data = "";
        try {
            data = MessageFormat.format((String) smsNoticeConfigurator.get("smsSend"), phone,
                URLEncoder.encode(message, charset));
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Exception occurs when composing POST data: phone({}), message({})", phone, message, e);
            throw new NoticeException(i18NService.getMessage("pep.notice.sms.send.error"));
        }
        String url = (String) smsNoticeConfigurator.get("smsUrl");
        String finalData = data;
        HttpClient.post(url, MediaType.APPLICATION_FORM_URLENCODED, data, new Callback() {
            @Override
            public void onSuccess(ResponseEntity<byte[]> responseEntity) {
                String resBody = new String(responseEntity.getBody(), Charset.forName(charset));
                if (!resBody.startsWith(SUCCESS)) {
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
    }

    @Override
    public void beforeSend(BusinessNotice notice) throws NoticeException {
        boolean result = smsLimitCheckService.couldSendSMS(notice.getTargetTo());
        if (!result) {
            throw new NoticeException(i18NService.getMessage("pep.notice.sms.get.frequently.try.again"));
        }
    }

    @Override
    public void afterSend(ReadOnlyNotice notice) {

    }

    @Override
    public NoticeStatus getStatus(ReadOnlyNotice notice) {
        return NoticeStatus.SUCCESS;
    }
}
