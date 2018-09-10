package com.proper.enterprise.platform.notice.server.push.handler.ios;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.server.api.exception.NoticeException;
import com.proper.enterprise.platform.notice.server.api.handler.NoticeSendHandler;
import com.proper.enterprise.platform.notice.server.api.model.BusinessNotice;
import com.proper.enterprise.platform.notice.server.api.model.ReadOnlyNotice;
import com.proper.enterprise.platform.notice.server.push.configurator.ios.IOSNoticeClient;
import com.proper.enterprise.platform.notice.server.push.handler.AbstractPushSendSupport;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;
import com.turo.pushy.apns.PushNotificationResponse;
import com.turo.pushy.apns.util.ApnsPayloadBuilder;
import com.turo.pushy.apns.util.SimpleApnsPushNotification;
import com.turo.pushy.apns.util.TokenUtil;
import io.netty.util.concurrent.Future;
import org.springframework.beans.factory.annotation.Autowired;

public class IOSNoticeSender extends AbstractPushSendSupport implements NoticeSendHandler {

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
        String payload = payloadBuilder.buildWithDefaultMaximumLength();
        String topic = iosNoticeClient.getPushPackage(notice.getAppKey());
        SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(TokenUtil
            .sanitizeTokenString(notice.getTargetTo()), topic, payload);
        final Future<PushNotificationResponse<SimpleApnsPushNotification>> sendNotificationFuture = iosNoticeClient
            .getClient(notice.getAppKey())
            .sendNotification(pushNotification);
        try {
            final PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse = sendNotificationFuture.get();
            if (pushNotificationResponse.isAccepted()) {
                return;
            }
            throw new NoticeException(pushNotificationResponse.getRejectionReason());
        } catch (Exception e) {
            throw new NoticeException("ios get response error", e);
        }
    }

    @Override
    public void beforeSend(BusinessNotice notice) throws NoticeException {
        iosNoticeClient.getClient(notice.getAppKey());
        if (StringUtil.isEmpty(iosNoticeClient.getPushPackage(notice.getAppKey()))) {
            throw new ErrMsgException("ios push can't send without pushPackage");
        }
    }

    @Override
    public void afterSend(ReadOnlyNotice notice) {

    }

    @Override
    public NoticeStatus getStatus(ReadOnlyNotice notice) throws NoticeException {
        return NoticeStatus.SUCCESS;
    }
}
