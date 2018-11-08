package com.proper.enterprise.platform.push.vendor;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.dfs.api.service.DFSService;
import com.proper.enterprise.platform.file.api.File;
import com.proper.enterprise.platform.file.repository.FileRepository;
import com.proper.enterprise.platform.push.common.model.enums.PushDeviceType;
import com.proper.enterprise.platform.push.common.model.enums.PushMode;
import com.proper.enterprise.platform.push.config.PushGlobalInfo;
import com.proper.enterprise.platform.push.repository.PushDeviceRepository;
import com.proper.enterprise.platform.push.repository.PushMsgRepository;
import com.proper.enterprise.platform.core.i18n.I18NService;
import org.nutz.mapl.Mapl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通过反射+工厂方法来应用“开放封闭原则”（对扩展开发，对修改封闭）
 */
@Component
public class PushVendorFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushVendorFactory.class);

    @Autowired
    private PushGlobalInfo globalInfo;
    @Autowired
    private PushMsgRepository msgRepo;
    @Autowired
    private PushDeviceRepository deviceRepo;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private DFSService dsfService;
    @Autowired
    private I18NService i18NService;

    private static final Map<String, AbstractPushVendorService> SERVICE_POOL = new ConcurrentHashMap<>();
    private static final String BASE_PACKAGE = PushVendorFactory.class.getPackage().getName();

    public synchronized AbstractPushVendorService getPushVendorService(String appKey, PushDeviceType deviceType,
                                                                       PushMode pushMode) {
        String key = composeKey(appKey, deviceType, pushMode);
        if (SERVICE_POOL.containsKey(key)) {
            LOGGER.debug("Current service pool has key set {}, contains {}", getKeySet(), key);
            return SERVICE_POOL.get(key);
        }

        try {
            String clazzName = BASE_PACKAGE + "." + deviceType + "." + pushMode + ".PushVendorServiceImpl";
            Class clazz = Class.forName(clazzName);
            AbstractPushVendorService service = (AbstractPushVendorService) clazz.newInstance();
            service.setAppkey(appKey);
            service.setDevicetype(deviceType);
            service.setPushMode(pushMode);
            service.setMsgRepo(msgRepo);
            service.setDeviceRepo(deviceRepo);
            service.setGlobalInfo(globalInfo);
            Object pushParams = Mapl.cell(globalInfo.getPushConfigs(),
                appKey + ".device." + deviceType + "." + pushMode);
            LOGGER.debug("Get pushParams of appKey({}), deviceType({}) and pushMode({}) is {}",
                appKey, deviceType, pushMode, JSONUtil.toJSONIgnoreException(pushParams));
            Object diplomaId = Mapl.cell(pushParams,
                "diplomaId");
            LOGGER.debug("DiplomaId of appKey({}), deviceType({}) and pushMode({}) is {}",
                appKey, deviceType, pushMode, diplomaId);
            if (diplomaId != null) {
                ((LinkedHashMap) pushParams).put("input", getInputStream(diplomaId.toString()));
            }
            service.setPushParams(pushParams);
            LOGGER.debug("Current service pool has key set {}, put service of {}", getKeySet(), key);
            SERVICE_POOL.put(key, service);
            return service;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return null;
    }

    private String getKeySet() {
        return Arrays.toString(SERVICE_POOL.keySet().toArray());
    }

    private String composeKey(String appKey, PushDeviceType deviceType, PushMode pushMode) {
        return appKey + "_" + deviceType.toString() + "_" + pushMode;
    }

    private InputStream getInputStream(String fileId) {
        File file = fileRepository.findById(fileId).<ErrMsgException>orElseThrow(() -> {
            LOGGER.error("Could NOT find file entity info of {}", fileId);
            throw new ErrMsgException(i18NService.getMessage("pep.file.download.not.find"));
        });
        InputStream inputStream = null;
        try {
            inputStream = dsfService.getFile(file.getFilePath());
            if (inputStream == null) {
                LOGGER.error("Could NOT get input stream of {} from DFS service", file.getFilePath());
                throw new ErrMsgException(i18NService.getMessage("pep.file.download.not.find"));
            }
        } catch (IOException ioe) {
            LOGGER.error("Error occurs when getting input stream of file " + JSONUtil.toJSONIgnoreException(file), ioe);
        }
        return inputStream;
    }
}
