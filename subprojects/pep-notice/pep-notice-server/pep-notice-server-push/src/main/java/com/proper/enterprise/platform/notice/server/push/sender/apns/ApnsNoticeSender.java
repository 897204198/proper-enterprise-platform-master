package com.proper.enterprise.platform.notice.server.push.sender.apns;

import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.server.api.handler.NoticeSendHandler;
import com.proper.enterprise.platform.notice.server.api.model.BusinessNotice;
import com.proper.enterprise.platform.notice.server.api.model.BusinessNoticeResult;
import com.proper.enterprise.platform.notice.server.api.model.ReadOnlyNotice;
import com.proper.enterprise.platform.notice.server.api.util.ThrowableMessageUtil;
import com.proper.enterprise.platform.notice.server.push.client.apns.ApnsNoticeClientManagerApi;
import com.proper.enterprise.platform.notice.server.push.configurator.BasePushConfigApi;
import com.proper.enterprise.platform.notice.server.push.convert.PushMsgConvert;
import com.proper.enterprise.platform.notice.server.push.dao.entity.PushNoticeMsgEntity;
import com.proper.enterprise.platform.notice.server.sdk.enums.PushChannelEnum;
import com.proper.enterprise.platform.notice.server.push.enums.apns.IOSErrCodeEnum;
import com.proper.enterprise.platform.notice.server.push.sender.AbstractPushSendSupport;
import com.proper.enterprise.platform.notice.server.sdk.constants.NoticeErrorCodeConstants;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;
import com.turo.pushy.apns.PushNotificationResponse;
import com.turo.pushy.apns.util.ApnsPayloadBuilder;
import com.turo.pushy.apns.util.SimpleApnsPushNotification;
import com.turo.pushy.apns.util.TokenUtil;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("iosNoticeSender")
public class ApnsNoticeSender extends AbstractPushSendSupport implements NoticeSendHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApnsNoticeSender.class);


    private ApnsNoticeClientManagerApi iosNoticeClientManager;

    @Autowired
    @Qualifier("iosNoticeConfigurator")
    private BasePushConfigApi iosNoticeConfigurator;


    @Autowired
    public ApnsNoticeSender(ApnsNoticeClientManagerApi iosNoticeClientManager) {
        this.iosNoticeClientManager = iosNoticeClientManager;
    }

    @Override
    public BusinessNoticeResult send(ReadOnlyNotice notice) {
        PushNoticeMsgEntity pushNoticeMsgEntity = PushMsgConvert.convert(notice);
        pushNoticeMsgEntity.setPushChannel(PushChannelEnum.APNS);
        pushNoticeMsgEntity.setDeviceType(PushMsgConvert.convert(PushChannelEnum.APNS));
        pushNoticeMsgEntity.setStatus(NoticeStatus.PENDING);
        PushNoticeMsgEntity savePushNoticeMsgEntity = super.saveOrUpdatePushMsg(pushNoticeMsgEntity);
        try {
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
            String topic = iosNoticeConfigurator.getPushPackage(notice.getAppKey(), PushChannelEnum.APNS);
            SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(TokenUtil
                .sanitizeTokenString(notice.getTargetTo()), topic, payload);
            final Future<PushNotificationResponse<SimpleApnsPushNotification>> sendNotificationFuture = iosNoticeClientManager
                .get(notice.getAppKey())
                .sendNotification(pushNotification);
            final PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse = sendNotificationFuture.get();
            if (pushNotificationResponse.isAccepted()) {
                savePushNoticeMsgEntity.setStatus(NoticeStatus.SUCCESS);
                savePushNoticeMsgEntity.setMessageId(pushNotificationResponse.getApnsId().toString());
                super.saveOrUpdatePushMsg(savePushNoticeMsgEntity);
                return new BusinessNoticeResult(NoticeStatus.SUCCESS);
            }
            super.updateStatus(savePushNoticeMsgEntity.getId(), NoticeStatus.FAIL,
                IOSErrCodeEnum.convertErrorCode(pushNotificationResponse.getRejectionReason()),
                pushNotificationResponse.getRejectionReason());
            return new BusinessNoticeResult(NoticeStatus.FAIL,
                IOSErrCodeEnum.convertErrorCode(pushNotificationResponse.getRejectionReason()),
                pushNotificationResponse.getRejectionReason());
        } catch (Exception e) {
            LOGGER.error("apns get response error", e);
            super.updateStatus(savePushNoticeMsgEntity.getId(), NoticeStatus.FAIL,
                e.getMessage(), ThrowableMessageUtil.getStackTrace(e));
            return new BusinessNoticeResult(NoticeStatus.FAIL, e.getMessage(), ThrowableMessageUtil.getStackTrace(e));
        }
    }

    @Override
    public BusinessNoticeResult beforeSend(BusinessNotice notice) {
        try {
            iosNoticeClientManager.get(notice.getAppKey());
        } catch (Exception e) {
            return new BusinessNoticeResult(NoticeStatus.FAIL, NoticeErrorCodeConstants.CHECK_ERROR,
                ThrowableMessageUtil.getStackTrace(e));
        }
        if (StringUtil.isEmpty(iosNoticeConfigurator.getPushPackage(notice.getAppKey(), PushChannelEnum.APNS))) {
            return new BusinessNoticeResult(NoticeStatus.FAIL, NoticeErrorCodeConstants.CHECK_ERROR,
                "apns push can't send without pushPackage");
        }
        return new BusinessNoticeResult(NoticeStatus.SUCCESS);
    }

    @Override
    public void afterSend(ReadOnlyNotice notice) {

    }

    @Override
    public BusinessNoticeResult getStatus(ReadOnlyNotice notice) {
        return super.getStatus(notice);
    }
}
