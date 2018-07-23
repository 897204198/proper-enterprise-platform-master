package com.proper.enterprise.platform.push.config;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.utils.ConfCenter;
import com.proper.enterprise.platform.push.api.openapi.service.PushChannelService;
import com.proper.enterprise.platform.push.vo.PushChannelVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class PushGlobalInfo {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushGlobalInfo.class);

    public static final String JSM_DES_APP_SERVER_REQUEST = "properpush/jms/AppServerRequest";

    @Autowired
    PushChannelService pushChannelService;

    private Map<String, Map<String, Object>> appconfigs;


    public Map<String, Map<String, Object>> getPushConfigs() {
        appconfigs = new HashMap<>(16);
        DataTrunk<PushChannelVO> all = pushChannelService.findAll();
        if (all != null && all.getCount() > 0) {
            for (PushChannelVO pushChannelVO : all.getData()) {
                appconfigs.put(pushChannelVO.getChannelName(), convertMap(pushChannelVO));
            }
        }
        return appconfigs;
    }

    /**
     * 获取数据库的批处理大小
     *
     * @return 现在默认1000
     */
    public int getDbBatchSize() {
        return Integer.parseInt(ConfCenter.get("pep_push_db_batch_size"));
    }

    private Map<String, Object> convertMap(PushChannelVO pushChannelVO) {
        Map<String, Object> channelMap = new LinkedHashMap<>();
        channelMap.put("desc", pushChannelVO.getChannelDesc());
        channelMap.put("msg_save_days", pushChannelVO.getMsgSaveDays());
        channelMap.put("max_send_count", pushChannelVO.getMaxSendCount());
        channelMap.put("secretKey", pushChannelVO.getSecretKey());

        Map<String, Object> deviceMap = new LinkedHashMap<>();
        Map<String, Object> androidMap = getAndroidMap(pushChannelVO);
        if (androidMap != null) {
            deviceMap.put("android", androidMap);
        }
        Map<String, Object> iosMap = getIosMap(pushChannelVO);
        if (iosMap != null) {
            deviceMap.put("ios", iosMap);
        }
        channelMap.put("device", deviceMap);
        return channelMap;
    }

    private Map<String, Object> getAndroidMap(PushChannelVO pushChannelVO) {
        if (pushChannelVO.getAndroid() == null) {
            return null;
        }
        Map<String, Object> huaweiMap = new LinkedHashMap<>();
        huaweiMap.put("the_app_id", pushChannelVO.getAndroid().getHuawei().getTheAppId());
        huaweiMap.put("the_app_secret", pushChannelVO.getAndroid().getHuawei().getTheAppSecret());
        huaweiMap.put("the_app_package", pushChannelVO.getAndroid().getHuawei().getTheAppPackage());

        Map<String, Object> xiaomiMap = new LinkedHashMap<>();
        xiaomiMap.put("the_app_secret", pushChannelVO.getAndroid().getXiaomi().getTheAppSecret());
        xiaomiMap.put("the_app_package", pushChannelVO.getAndroid().getXiaomi().getTheAppPackage());

        Map<String, Object> androidMap = new LinkedHashMap<>();
        androidMap.put("huawei", huaweiMap);
        androidMap.put("xiaomi", xiaomiMap);
        return androidMap;
    }

    private Map<String, Object> getIosMap(PushChannelVO pushChannelVO) {
        if (pushChannelVO.getIos() == null) {
            return null;
        }
        Map<String, Object> apnsMap = new LinkedHashMap<>();
        apnsMap.put("env_product", pushChannelVO.getIos().isEnvProduct());
        apnsMap.put("keystore_password", pushChannelVO.getIos().getKeystorePassword());
        apnsMap.put("topic", pushChannelVO.getIos().getTopic());
        apnsMap.put("diplomaId", pushChannelVO.getDiplomaId());
        Map<String, Object> iosMap = new LinkedHashMap<>();
        iosMap.put("apns", apnsMap);
        return iosMap;
    }

}
