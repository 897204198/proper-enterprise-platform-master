package com.proper.enterprise.platform.notice.server.push.handler.ios;

import com.proper.enterprise.platform.notice.server.api.exception.NoticeException;
import com.proper.enterprise.platform.notice.server.api.handler.NoticeSendHandler;
import com.proper.enterprise.platform.notice.server.api.model.BusinessNotice;
import com.proper.enterprise.platform.notice.server.api.model.ReadOnlyNotice;
import com.proper.enterprise.platform.notice.server.push.configurator.ios.IOSNoticeClient;
import com.proper.enterprise.platform.notice.server.push.handler.AbstractPushNoticeSender;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;
import com.turo.pushy.apns.util.ApnsPayloadBuilder;
import com.turo.pushy.apns.util.SimpleApnsPushNotification;
import org.springframework.beans.factory.annotation.Autowired;

public class IOSNoticeSender extends AbstractPushNoticeSender implements NoticeSendHandler {

    private IOSNoticeClient iosNoticeClient;

    @Autowired
    public IOSNoticeSender(IOSNoticeClient iosNoticeClient) {
        this.iosNoticeClient = iosNoticeClient;
    }

    @Override
    public void send(ReadOnlyNotice notice) throws NoticeException {
        ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();
        //设置内容
        payloadBuilder.setAlertBody(notice.getContent());
        //设置标题
        payloadBuilder.setAlertTitle(notice.getTitle());
        //设置自定义属性
        payloadBuilder.addCustomProperty(CUSTOM_PROPERTY_KEY, notice.getNoticeExtMsgMap());
        // 默认声音
        payloadBuilder.setSound(ApnsPayloadBuilder.DEFAULT_SOUND_FILENAME);
        // 设置应用的角标数
        Integer badgeNumber = getBadgeNumber(notice);
        if (badgeNumber != null) {
            payloadBuilder.setBadgeNumber(badgeNumber);
        }
        final String payload = payloadBuilder.buildWithDefaultMaximumLength();
        SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(notice.getTargetTo(), "a", payload);
        iosNoticeClient.getClient(notice.getAppKey()).sendNotification(pushNotification);
    }

    @Override
    public void beforeSend(BusinessNotice notice) throws NoticeException {

    }

    @Override
    public void afterSend(ReadOnlyNotice notice) {

    }

    @Override
    public NoticeStatus getStatus(ReadOnlyNotice notice) throws NoticeException {
        return null;
    }
}
