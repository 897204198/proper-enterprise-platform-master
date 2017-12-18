package com.proper.enterprise.platform.push.vendor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.nutz.mapl.Mapl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.proper.enterprise.platform.push.config.PushGlobalInfo;
import com.proper.enterprise.platform.push.repository.PushDeviceRepository;
import com.proper.enterprise.platform.push.repository.PushMsgRepository;
import com.proper.enterprise.platform.push.common.model.enums.PushDeviceType;
import com.proper.enterprise.platform.push.common.model.enums.PushMode;

/**
 * 通过反射+工厂方法来应用“开放封闭原则”（对扩展开发，对修改封闭）
 *
 * @author shen
 *
 */
@Component
public class PushVendorFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushVendorFactory.class);
    @Autowired
    PushGlobalInfo globalInfo;
    @Autowired
    PushMsgRepository msgRepo;
    @Autowired
    PushDeviceRepository deviceRepo;
    final Map<String, AbstractPushVendorService> mapPushVendorServices = new ConcurrentHashMap<String, AbstractPushVendorService>();
    public static final String BASE_PACKAGE = PushVendorFactory.class.getPackage().getName();

    public synchronized AbstractPushVendorService getPushVendorService(String appkey, PushDeviceType devicetype,
            PushMode pushMode) {
        AbstractPushVendorService service = null;
        String key = getKey(appkey, devicetype, pushMode);
        service = mapPushVendorServices.get(key);
        if (service == null) {
            try {
                String clazzName = BASE_PACKAGE + "." + devicetype + "." + pushMode + ".PushVendorServiceImpl";
                Class clazz = Class.forName(clazzName);
                service = (AbstractPushVendorService) clazz.newInstance();
                service.setAppkey(appkey);
                service.setDevicetype(devicetype);
                service.setPushMode(pushMode);
                service.setMsgRepo(msgRepo);
                service.setDeviceRepo(deviceRepo);
                service.setGlobalInfo(globalInfo);
                Object pushParams = Mapl.cell(globalInfo.getPushConfigs(),
                        appkey + ".device." + devicetype + "." + pushMode);
                service.setPushParams(pushParams);
                mapPushVendorServices.put(key, service);
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage(), ex);
                service = null;
            }
        }
        return service;
    }

    /**
     * 生成发送推送对应的缓存的key，用于mapSendConnections
     *
     * @param appkey
     * @param devicetype
     * @param pushMode
     * @return
     */
    public String getKey(String appkey, PushDeviceType devicetype, PushMode pushMode) {
        StringBuilder sb = new StringBuilder();
        sb.append(appkey).append("_").append(devicetype.toString()).append("_").append(pushMode);
        return sb.toString();
    }
}
