package com.proper.enterprise.platform.notice.server.push.sender.xiaomi;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.server.api.exception.NoticeException;
import com.proper.enterprise.platform.notice.server.api.handler.NoticeSendHandler;
import com.proper.enterprise.platform.notice.server.api.model.BusinessNotice;
import com.proper.enterprise.platform.notice.server.api.model.ReadOnlyNotice;
import com.proper.enterprise.platform.notice.server.push.client.xiaomi.XiaomiNoticeClientManagerApi;
import com.proper.enterprise.platform.notice.server.push.configurator.BasePushConfigApi;
import com.proper.enterprise.platform.notice.server.push.enums.PushChannelEnum;
import com.proper.enterprise.platform.notice.server.push.sender.AbstractPushSendSupport;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;
import com.xiaomi.push.sdk.ErrorCode;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Result;
import com.xiaomi.xmpush.server.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import static java.lang.Math.*;

@Service("xiaomiNoticeSender")
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

    /**
     * 通知栏要显示多条推送消息需配置不同的notifyId
     */
    private static final int MIN_NOTIFY_ID = 10000;

    private int notifyId = MIN_NOTIFY_ID;

    @Autowired
    private XiaomiNoticeClientManagerApi xiaomiNoticeClient;

    private BasePushConfigApi xiaomiNoticeConfigurator;

    @Autowired
    public XiaomiNoticeSender(BasePushConfigApi xiaomiNoticeConfigurator) {
        this.xiaomiNoticeConfigurator = xiaomiNoticeConfigurator;
    }

    @Override
    public void send(ReadOnlyNotice notice) throws NoticeException {
        Message message = buildMessage(notice);
        Sender sender = xiaomiNoticeClient.getClient(notice.getAppKey());
        LOGGER.debug("xiaomi push check Message :{}", JSONUtil.toJSONIgnoreException(message));
        try {
            Result result = sender.send(message, notice.getTargetTo(), 1);
            if (result.getErrorCode() != ErrorCode.Success) {
                LOGGER.error("xiaomi push send message fail  pushId:{}, rsp:{}", notice.getId(), JSONUtil.toJSONIgnoreException(result));
            }
        } catch (Exception e) {
            LOGGER.error("error xiaomi push  notice:{}, exception:{}", JSONUtil.toJSONIgnoreException(notice), e);
            throw new NoticeException("xiaomi push send message exception", e);
        }

    }

    private Message buildMessage(ReadOnlyNotice notice) {
        Message.Builder msgBuilder = new Message.Builder();
        // 推送标题
        msgBuilder.title(StringUtil.abbreviate(notice.getTitle(), TITLE_MAX_LENGTH));
        // 推送详情描述
        msgBuilder.description(StringUtil.abbreviate(notice.getContent(), DESCRIPTION_MAX_LENGTH));
        // 设置app的包名packageName
        msgBuilder.restrictedPackageName(xiaomiNoticeConfigurator.getPushPackage(notice.getAppKey(), PushChannelEnum.XIAOMI));
        // 推送提醒方式
        msgBuilder.notifyType(1);
        // 通知栏要显示多条推送消息需配置不同的notifyId
        msgBuilder.notifyId(getNextNotifyId());
        if (isCmdMessage(notice)) {
            // 消息使用透传消息
            msgBuilder.passThrough(1);
        } else {
            // 消息使用通知栏
            msgBuilder.passThrough(0);
        }
        // 获取角标数
        Integer badgeNumber = getBadgeNumber(notice);
        // 获取自定义配置
        Map msg = this.getCustomProperty(notice);
        if (null != badgeNumber) {
            // 系统消息类型：设置角标
            msg.put("_proper_mpage", "badge");
            // 需要手机端自己生成一个notification通知
            // 因为在小米手机的设置角标接口里，角标接口是与通知栏消息绑定在一起的，需要程序自己发送notification,并带上角标数
            msg.put("_proper_badge_type", "notification");
            msg.put("_proper_badge", badgeNumber);
            msg.remove("uri");
        }
        msgBuilder.payload(JSONUtil.toJSONIgnoreException(msg));
        return msgBuilder.build();
    }

    @Override
    public void beforeSend(BusinessNotice notice) throws NoticeException {
        if (StringUtil.isNull(xiaomiNoticeConfigurator.getPushPackage(notice.getAppKey(), PushChannelEnum.XIAOMI))) {
            throw new ErrMsgException("xiaomi push need pushPackage");
        }
    }

    @Override
    public void afterSend(ReadOnlyNotice notice) {

    }

    @Override
    public NoticeStatus getStatus(ReadOnlyNotice notice) throws NoticeException {
        return NoticeStatus.SUCCESS;
    }

    /**
     * 设置不同的 NOTIFY_ID 以便于显示多条推送
     *
     * @return NOTIFY_ID
     */
    private synchronized int getNextNotifyId() {
        if (notifyId <= MIN_NOTIFY_ID || notifyId == Integer.MAX_VALUE) {
            notifyId = MIN_NOTIFY_ID;
        }
        return (int) (random() * 9000) + 1000 + notifyId++;
    }
}
