package com.proper.enterprise.platform.push.openapi.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.push.entity.PushDeviceEntity;
import com.proper.enterprise.platform.push.entity.PushMsgEntity;
import com.proper.enterprise.platform.push.entity.PushUserEntity;
import com.proper.enterprise.platform.push.repository.PushDeviceRepository;
import com.proper.enterprise.platform.push.repository.PushMsgRepository;
import com.proper.enterprise.platform.push.repository.PushUserRepository;
import org.nutz.mapl.Mapl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.push.api.openapi.exceptions.PushException;
import com.proper.enterprise.platform.push.api.openapi.model.PushMessage;
import com.proper.enterprise.platform.push.api.openapi.service.AppServerRequestService;
import com.proper.enterprise.platform.push.config.PushGlobalInfo;
import com.proper.enterprise.platform.push.common.model.enums.PushDeviceStatus;
import com.proper.enterprise.platform.push.common.model.enums.PushDeviceType;
import com.proper.enterprise.platform.push.common.model.enums.PushMode;
import com.proper.enterprise.platform.push.common.model.enums.PushMsgStatus;
import com.proper.enterprise.platform.push.vendor.AbstractPushVendorService;
import com.proper.enterprise.platform.push.vendor.PushVendorFactory;

@Service
public class AppServerRequestServiceImpl implements AppServerRequestService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppServerRequestServiceImpl.class);
    @Autowired
    PushGlobalInfo globalInfo;
    @Autowired
    PushUserRepository userRepo;
    @Autowired
    PushDeviceRepository deviceRepo;
    @Autowired
    PushMsgRepository msgRepo;
    @Autowired
    PushVendorFactory pushVendorFactory;

    @Override
    public void savePushMessageToAllUsers(String appkey, PushMessage thePushmsg) {
        final int dbBatchSize = globalInfo.getDbBatchSize();
        Pageable pageable = new PageRequest(0, dbBatchSize);
        List<String> lstUids = new ArrayList<>(dbBatchSize);
        boolean hasData;
        do {
            Page<PushUserEntity> page = userRepo.findByAppkey(appkey, pageable);
            pageable = pageable.next();

            hasData = page.hasContent();
            if (hasData) {
                List<PushUserEntity> lstUser = page.getContent();
                lstUids.clear();
                for (PushUserEntity u : lstUser) {
                    lstUids.add(u.getUserid());
                }
                savePushMessageToUsers(appkey, lstUids, thePushmsg);
            }
        } while (hasData);

    }

    @Override
    public void savePushMessageToAllDevices(String appkey, PushDeviceType deviceType0, PushMessage thePushmsg) {
        Map<String, Object> deviceConf = (Map<String, Object>) Mapl.cell(globalInfo.getPushConfigs(),
            appkey + ".device");
        if (deviceConf == null) {
            throw new PushException("appkey: " + appkey + " is not valid!");
        }
        final int dbBatchSize = globalInfo.getDbBatchSize();
        List<String> lstDids = new ArrayList<>(dbBatchSize);
        boolean hasData = false;
        for (String strDeviceType : deviceConf.keySet()) {

            PushDeviceType deviceType = Enum.valueOf(PushDeviceType.class, strDeviceType.trim());
            // 传入的deviceType不为空，则只发送指定设备类型的消息
            if (deviceType0 != null) {
                if (deviceType != deviceType0) {
                    continue;
                }
            }
            Pageable pageable = new PageRequest(0, dbBatchSize);
            do {
                Page<PushDeviceEntity> page = deviceRepo.findByAppkeyAndDevicetypeAndMstatus(appkey, deviceType,
                    PushDeviceStatus.VALID, pageable);
                pageable = pageable.next();

                hasData = page.hasContent();
                if (hasData) {
                    List<PushDeviceEntity> lstUser = page.getContent();
                    lstDids.clear();
                    for (PushDeviceEntity u : lstUser) {
                        lstDids.add(u.getDeviceid());
                    }
                    savePushMessageToDevices(appkey, deviceType, lstDids, thePushmsg);
                }
            } while (hasData);
            // 传入的deviceType不为空，则只发送指定设备类型的消息
            if (deviceType0 != null) {
                break;
            }
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    public void savePushMessageToUsers(String appkey, List<String> lstUids, PushMessage thePushmsg) {
        Map<String, Object> rtn = new LinkedHashMap<>();
        Map<String, Object> deviceConf = (Map<String, Object>) Mapl.cell(globalInfo.getPushConfigs(),
            appkey + ".device");
        if (deviceConf == null) {
            throw new PushException("appkey: " + appkey + " is not valid!");
        }

        for (Entry<String, Object> entry : deviceConf.entrySet()) {
            String strDeviceType = entry.getKey();
            PushDeviceType deviceType = Enum.valueOf(PushDeviceType.class, strDeviceType.trim());
            Map<String, Object> mapPushModes = (Map<String, Object>) entry.getValue();
            for (String strPushMode : mapPushModes.keySet()) {
                PushMode pushMode = Enum.valueOf(PushMode.class, strPushMode.trim());
                AbstractPushVendorService pushService = pushVendorFactory.getPushVendorService(appkey, deviceType,
                    pushMode);
                final List<PushMsgEntity> lstMsgs = new ArrayList<PushMsgEntity>();
                List<PushDeviceEntity> lstDevices = null;
                // 查找存在的用户id对应的有效的设备
                if (CollectionUtil.isNotEmpty(lstUids)) {
                    lstDevices = deviceRepo.findByAppkeyAndDevicetypeAndPushModeAndMstatusAndUseridIn(appkey,
                        deviceType, pushMode, PushDeviceStatus.VALID, lstUids);
                    doSendMsgToDevices(appkey, thePushmsg, rtn, deviceType, pushMode, pushService, lstMsgs, lstDevices);
                } else {
                    LOGGER.error("savePushMessageToUsers but the userid is empty!");
                }

            }
        }
        LOGGER.trace(rtn.toString());
    }

    private void doSendMsgToDevices(String appkey, PushMessage thePushmsg, Map<String, Object> rtn, PushDeviceType deviceType, PushMode pushMode, AbstractPushVendorService pushService, List<PushMsgEntity> lstMsgs, List<PushDeviceEntity> lstDevices) {
        if (CollectionUtil.isNotEmpty(lstDevices)) {
            // 向数据库中插入数据message。
            for (PushDeviceEntity d : lstDevices) {
                if (StringUtil.isNotEmpty(d.getPushToken())) {
                    PushMsgEntity dbMsg = createMsgVO(thePushmsg, appkey, d);
                    lstMsgs.add(dbMsg);
                } else {
                    LOGGER.info("appkey:" + appkey + " device:" + d.getDeviceid() + " push token is empty!");
                }
            }
            msgRepo.save(lstMsgs);
        } else {
            LOGGER.info("doSendMsgToDevices,device is empty,then no need send msg!");
        }
        if (CollectionUtil.isNotEmpty(lstMsgs)) {
            int sendCount = pushService.pushMsg(lstMsgs);
            rtn.put(appkey + "_" + deviceType + "_" + pushMode, sendCount);
        } else {
            LOGGER.info("doSendMsgToDevices,lstMsgs is empty,then no need send!");
        }
    }

    private PushMsgEntity createMsgVO(final PushMessage msg, String appkey, PushDeviceEntity d) {
        PushMsgEntity dbmsg = new PushMsgEntity();
        dbmsg.setMsgid(UUID.randomUUID().toString());
        dbmsg.setMstatus(PushMsgStatus.UNSEND);
        dbmsg.setUserid(d.getUserid());
        dbmsg.setDevice(d);
        dbmsg.setAppkey(appkey);
        dbmsg.setMcontent(msg.getContent());
        dbmsg.setMcustomDatasMap(msg.getCustoms());
        dbmsg.setMtitle(msg.getTitle());
        dbmsg.setSendCount(0);
        // 添加全局统一的自定义键值对
        Map<String, Object> maps = dbmsg.getMcustomDatasMap();
        maps.put("_proper_userid", d.getUserid()); // userid
        maps.put("_proper_title", msg.getTitle()); // 消息标题
        maps.put("_proper_content", msg.getContent()); // 消息正文
        dbmsg.setMcustomDatasMap(maps);
        return dbmsg;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void savePushMessageToDevices(String appkey, PushDeviceType deviceType, List<String> lstDeviceids,
                                         PushMessage thePushmsg) {
        Map<String, Object> rtn = new LinkedHashMap<>();
        Map<String, Object> deviceConf = (Map<String, Object>) Mapl.cell(globalInfo.getPushConfigs(),
            appkey + ".device");
        if (deviceConf == null) {
            throw new PushException("appkey: " + appkey + " is not valid!");
        }
        Map<String, Object> mapPushModes = (Map<String, Object>) deviceConf.get(deviceType.toString());
        for (String strPushMode : mapPushModes.keySet()) {
            PushMode pushMode = Enum.valueOf(PushMode.class, strPushMode.trim());
            AbstractPushVendorService pushService = pushVendorFactory.getPushVendorService(appkey, deviceType,
                pushMode);
            final List<PushMsgEntity> lstMsgs = new ArrayList<PushMsgEntity>();
            List<PushDeviceEntity> lstDevices = null;
            // 查找存在的deviceid对应的有效的设备
            if (CollectionUtil.isNotEmpty(lstDeviceids)) {
                lstDevices = deviceRepo.findByAppkeyAndDevicetypeAndPushModeAndMstatusAndDeviceidIn(appkey, deviceType,
                    pushMode, PushDeviceStatus.VALID, lstDeviceids);
                doSendMsgToDevices(appkey, thePushmsg, rtn, deviceType, pushMode, pushService, lstMsgs, lstDevices);
            } else {
                LOGGER.error("savePushMessageToDevices but the deviceids is empty!");
            }
        }
        LOGGER.trace(rtn.toString());
    }

}
