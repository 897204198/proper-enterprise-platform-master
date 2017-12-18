package com.proper.enterprise.platform.push.vendor.android.xiaomi;

import java.util.Date;
import java.util.List;

import org.nutz.mapl.Mapl;

import com.proper.enterprise.platform.push.entity.PushMsgEntity;
import com.proper.enterprise.platform.push.common.model.enums.PushMsgStatus;
import com.proper.enterprise.platform.push.vendor.AbstractPushVendorService;

/**
 * 华为推送消息
 *
 * @author shen
 *
 */
public class PushVendorServiceImpl extends AbstractPushVendorService {
    XiaomiPushApp pushApp;
    private static final int MIN_NOTIFY_ID = 10000;
    private int notifyId = MIN_NOTIFY_ID;

    protected synchronized int getNextNotifyId() {
        if (notifyId <= MIN_NOTIFY_ID || notifyId == Integer.MAX_VALUE) {
            notifyId = MIN_NOTIFY_ID;
        }
        return notifyId++;
    }

    @Override
    public int pushMsg(List<PushMsgEntity> lstMsgs) {
        int sendCount = 0;
        if (pushApp == null) {
            pushApp = new XiaomiPushApp();
            pushApp.setTheAppPackage(Mapl.cell(pushParams, "the_app_package").toString());
            pushApp.setTheAppSecret(Mapl.cell(pushParams, "the_app_secret").toString());
            pushApp.setPushService(this);
        }
        int notifyId1 = getNextNotifyId();
        // 向指定的设备推送数据。
        for (PushMsgEntity dm : lstMsgs) {
            dm.setSendCount(dm.getSendCount() + 1); // 发送次数+1
            // 向手机端推送一条消息，手机端收到消息后，请求web服务器,再获取消息内容
            boolean r = pushApp.pushOneMsg(dm, notifyId1);
            if (r) {
                dm.setMstatus(PushMsgStatus.SENDED);
                dm.setMsendedDate(new Date());
                sendCount++;
            }
        }
        // 更新消息状态
        msgRepo.save(lstMsgs);
        return sendCount;
    }

}
