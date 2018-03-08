package com.proper.enterprise.platform.push.vendor.ios.apns;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.proper.enterprise.platform.core.utils.JSONUtil;
import org.nutz.mapl.Mapl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.proper.enterprise.platform.push.api.openapi.exceptions.PushException;
import com.proper.enterprise.platform.push.entity.PushMsgEntity;
import com.proper.enterprise.platform.push.common.model.enums.PushMsgStatus;
import com.proper.enterprise.platform.push.vendor.AbstractPushVendorService;

/**
 * ios apns 推送实现
 *
 * @author shen
 *
 */
public class PushVendorServiceImpl extends AbstractPushVendorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushVendorServiceImpl.class);
    ApnsPushApp pushApp;

    @Override
    public int pushMsg(List<PushMsgEntity> lstMsgs) {
        LOGGER.info("push log step5 ios pushMsg:msg:{}", JSONUtil.toJSONIgnoreException(lstMsgs));
        int sendCount = 0;
        if (pushApp == null) {
            pushApp = new ApnsPushApp();
            pushApp.setEnvProduct(Boolean.parseBoolean(Mapl.cell(pushParams, "env_product").toString()));
            pushApp.setKeyStorePassword(Mapl.cell(pushParams, "keystore_password").toString());
            pushApp.setTopic(Mapl.cell(pushParams, "topic").toString());
            String filename = Mapl.cell(pushParams, "keystore_filename").toString();
            ClassPathResource res = new ClassPathResource("/conf/push/vendor/apns/keystores/" + filename);
            Object keystoreMeta;
            try {
                keystoreMeta = res.getInputStream();
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
                throw new PushException("apns keystore file: " + filename + " can't find!");
            }
            // p12文件的绝对路径
            pushApp.setKeyStoreMeta(keystoreMeta);
            pushApp.setTheAppkey(appkey);
            pushApp.setPushService(this);
        }
        if (lstMsgs != null && lstMsgs.size() > 0) {
            // 向指定的设备推送数据。
            for (PushMsgEntity dm : lstMsgs) {
                dm.setSendCount(dm.getSendCount() + 1); // 发送次数+1
                // 向手机端推送一条消息
                boolean r = pushApp.pushOneMsg(dm);
                if (r) {
                    sendCount++;
                    dm.setMstatus(PushMsgStatus.SENDED);
                    dm.setMsendedDate(new Date());
                }
            }
            // 更新消息状态
            msgRepo.save(lstMsgs);

        }
        return sendCount;
    }

}
