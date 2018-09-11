package com.proper.enterprise.platform.notice.server.push.handler.xiaomi;


import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.server.api.exception.NoticeException;
import com.proper.enterprise.platform.notice.server.api.handler.NoticeSendHandler;
import com.proper.enterprise.platform.notice.server.api.model.BusinessNotice;
import com.proper.enterprise.platform.notice.server.api.model.ReadOnlyNotice;
import com.proper.enterprise.platform.notice.server.push.configurator.xiaomi.XiaomiNoticeClient;
import com.proper.enterprise.platform.notice.server.push.handler.AbstractPushSendSupport;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;
import com.xiaomi.push.sdk.ErrorCode;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Result;
import com.xiaomi.xmpush.server.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class XiaomiNoticeSender extends AbstractPushSendSupport implements NoticeSendHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(XiaomiNoticeSender.class);
    /**
     * 标题最大长度
     */
    private static final int TITLE_MAX_LENGTH = 15;
    /**
     * 描述最大长度
     */
    private static final int DESCRIPTION_MAX_LENGTH = 127;

    private static final int MIN_NOTIFY_ID = 10000;

    private int notifyId = MIN_NOTIFY_ID;
    private XiaomiNoticeClient xiaomiNoticeClient;

    @Autowired
    public XiaomiNoticeSender(XiaomiNoticeClient xiaomiNoticeClient) {
        this.xiaomiNoticeClient = xiaomiNoticeClient;
    }

    @Override
    public void send(ReadOnlyNotice notice) throws NoticeException {
        Message message = buildMessage(notice);
        Sender sender = xiaomiNoticeClient.getClient(notice.getAppKey());
        try {
            LOGGER.debug("xiaomi push check Message :{}", JSONUtil.toJSONIgnoreException(message));
            Result result = sender.send(message, notice.getTargetTo(), 1);
            if (result.getErrorCode() == ErrorCode.Success) {
                LOGGER.debug("success xiaomi push  pushId:{}, rsp:{}", notice.getId(), JSONUtil.toJSONIgnoreException(result));
            } else {
                LOGGER.error("error xiaomi push  pushId:{}, rsp:{}", notice.getId(), JSONUtil.toJSONIgnoreException(result));
            }

        } catch (Exception e) {
            throw new NoticeException("xiaomi push send message fail", e);
        }

    }

    private Message buildMessage(ReadOnlyNotice notice) {
        Message.Builder msgBuilder = new Message.Builder();
        // 推送标题
        msgBuilder.title(StringUtil.abbreviate(notice.getTitle(), TITLE_MAX_LENGTH));
        // 推送详情描述
        msgBuilder.description(StringUtil.abbreviate(notice.getContent(), DESCRIPTION_MAX_LENGTH));
        // 设置app的包名packageName
        msgBuilder.restrictedPackageName(xiaomiNoticeClient.getPushPackage(notice.getAppKey()));
        // 推送提醒方式
        msgBuilder.notifyType(1);
        // 通知栏要显示多条推送消息需配置不同的notifyId
        msgBuilder.notifyId(getNextNotifyId());
        if (isCmdMessage(notice)) {
            // 消息使用透传消息
            msgBuilder.passThrough(1);
        } else {
            Integer badgeNumber = getBadgeNumber(notice);
            if (null != badgeNumber) {
                notice.getNoticeExtMsgMap().put(BADGE_NUMBER_KEY, badgeNumber);
            }
            // 消息使用通知栏
            msgBuilder.passThrough(0);
        }
        //TODO 获取自定义配置
        msgBuilder.payload(JSONUtil.toJSONIgnoreException(this.getCustomProperty(notice)));
        return msgBuilder.build();
    }

    @Override
    public void beforeSend(BusinessNotice notice) throws NoticeException {
        if (StringUtil.isNull(xiaomiNoticeClient.getPushPackage(notice.getAppKey()))) {
            throw new ErrMsgException("xiaomi push need pushPackage");
        }
    }

    @Override
    public void afterSend(ReadOnlyNotice notice) {

    }

    @Override
    public NoticeStatus getStatus(ReadOnlyNotice notice) throws NoticeException {
        return null;
    }

    private synchronized int getNextNotifyId() {
        if (notifyId <= MIN_NOTIFY_ID || notifyId == Integer.MAX_VALUE) {
            notifyId = MIN_NOTIFY_ID;
        }
        return notifyId++;
    }
}
