package com.proper.enterprise.platform.push.vendor.android.huawei;

import com.proper.enterprise.platform.push.common.model.enums.PushMsgStatus;
import com.proper.enterprise.platform.push.entity.PushMsgEntity;
import com.proper.enterprise.platform.push.vendor.AbstractPushVendorService;
import org.nutz.mapl.Mapl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * 华为推送消息
 *
 * @author shen
 */
public class PushVendorServiceImpl extends AbstractPushVendorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushVendorServiceImpl.class);

    HuaweiPushApp pushApp;

    @Override
    public int pushMsg(List<PushMsgEntity> lstMsgs) {
        int sendCount = 0;
        if (pushApp == null) {
            pushApp = new HuaweiPushApp();
            pushApp.setTheAppid(Mapl.cell(pushParams, "the_app_id").toString());
            pushApp.setTheAppSecret(Mapl.cell(pushParams, "the_app_secret").toString());
            pushApp.setPackageName(Mapl.cell(pushParams, "the_app_package").toString());
            pushApp.setPushService(this);
        }

        // 向指定的设备推送数据。
        for (PushMsgEntity dm : lstMsgs) {
            LOGGER.info("huawei push log step5 pushId:{},userId:{}", dm.getId(), dm.getUserid());
            // 发送次数+1
            dm.setSendCount(dm.getSendCount() + 1);
            // 向手机端推送一条消息，手机端收到消息后，请求web服务器,再获取消息内容
            boolean r = pushApp.pushOneMsg(dm);
            if (r) {
                dm.setMstatus(PushMsgStatus.SENDED);
                dm.setMsendedDate(new Date());
                sendCount++;
            }
        }
        // 更新消息状态
        msgRepo.saveAll(lstMsgs);
        return sendCount;
    }

}
