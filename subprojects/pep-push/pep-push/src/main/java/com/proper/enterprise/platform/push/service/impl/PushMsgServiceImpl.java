package com.proper.enterprise.platform.push.service.impl;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.jpa.service.impl.AbstractJpaServiceSupport;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.push.api.PushMsg;
import com.proper.enterprise.platform.push.api.openapi.exceptions.PushException;
import com.proper.enterprise.platform.push.api.openapi.model.PushMessage;
import com.proper.enterprise.platform.push.common.model.enums.PushDeviceStatus;
import com.proper.enterprise.platform.push.common.model.enums.PushDeviceType;
import com.proper.enterprise.platform.push.common.model.enums.PushMode;
import com.proper.enterprise.platform.push.config.PushGlobalInfo;
import com.proper.enterprise.platform.push.entity.PushDeviceEntity;
import com.proper.enterprise.platform.push.entity.PushMsgEntity;
import com.proper.enterprise.platform.push.entity.PushUserEntity;
import com.proper.enterprise.platform.push.repository.PushDeviceRepository;
import com.proper.enterprise.platform.push.repository.PushMsgRepository;
import com.proper.enterprise.platform.push.repository.PushUserRepository;
import com.proper.enterprise.platform.push.service.PushMsgService;
import com.proper.enterprise.platform.push.vendor.AbstractPushVendorService;
import com.proper.enterprise.platform.push.vendor.PushVendorFactory;
import com.proper.enterprise.platform.push.vo.PushMsgVO;
import com.proper.enterprise.platform.sys.datadic.DataDic;
import com.proper.enterprise.platform.sys.datadic.util.DataDicUtil;
import org.nutz.json.Json;
import org.nutz.mapl.Mapl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PushMsgServiceImpl extends AbstractJpaServiceSupport<PushMsg, PushMsgRepository, String>
    implements PushMsgService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushMsgServiceImpl.class);

    @Autowired
    PushGlobalInfo globalInfo;
    @Autowired
    PushDeviceRepository deviceRepo;
    @Autowired
    PushVendorFactory pushVendorFactory;
    @Autowired
    PushUserRepository pushUserRepository;
    @Autowired
    private PushMsgRepository pushMsgRepository;

    public PushMsgRepository getRepository() {
        return pushMsgRepository;
    }

    @Override
    public DataTrunk<? extends PushMsg> findByDateTypeAndAppkey(Example example, Pageable pageable) {
        Page<PushMsgEntity> page = pushMsgRepository.findAll(example, pageable);
        List<PushMsgEntity> msgEntities = page.getContent();
        for (PushMsgEntity pushMsgEntity : msgEntities) {
            pushMsgEntity.setMcontent(StringUtil.substring(pushMsgEntity.getMcontent(), 0, 10));
            pushMsgEntity.setUserid(pushMsgEntity.getUserid().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
        }
        DataTrunk<? extends PushMsg> trunk = new DataTrunk<>(convertVo(page), page.getTotalElements());
        return trunk;
    }

    @Override
    public List savePushMessageToUsers(Map<String, String> requestParams) {
        List<String> idList = new ArrayList<>();
        String userids = requestParams.get("userids");
        String appkey = requestParams.get("appkey");
        final List<String> lstUids = Json.fromJsonAsList(String.class, userids);
        final PushMessage thePushmsg = getPushMessage(requestParams);

        Map<String, Object> deviceConf = (Map<String, Object>) Mapl.cell(globalInfo.getPushConfigs(),
            appkey + ".device");
        if (deviceConf == null) {
            throw new PushException("appkey: " + appkey + " is not valid!msg:" + JSONUtil.toJSONIgnoreException(thePushmsg));
        }

        for (Map.Entry<String, Object> entry : deviceConf.entrySet()) {
            String strDeviceType = entry.getKey();
            PushDeviceType deviceType = Enum.valueOf(PushDeviceType.class, strDeviceType.trim());
            Map<String, Object> mapPushModes = (Map<String, Object>) entry.getValue();
            for (String strPushMode : mapPushModes.keySet()) {
                PushMode pushMode = Enum.valueOf(PushMode.class, strPushMode.trim());
                AbstractPushVendorService pushService = pushVendorFactory.getPushVendorService(appkey, deviceType,
                    pushMode);
                List<PushDeviceEntity> lstDevices = null;
                // 查找存在的用户id对应的有效的设备
                if (CollectionUtil.isNotEmpty(lstUids)) {
                    lstDevices = deviceRepo.findByAppkeyAndDevicetypeAndPushModeAndMstatusAndUseridIn(appkey,
                        deviceType, pushMode, PushDeviceStatus.VALID, lstUids);
                    List<PushMsgEntity> list = doSavePushMsg(appkey, thePushmsg, deviceType, pushMode, lstDevices);
                    list.forEach(n -> idList.add(n.getId()));
                } else {
                    LOGGER.error("savePushMessageToUsers but the userid is empty!msg:{}", JSONUtil.toJSONIgnoreException(thePushmsg));
                }

            }
        }
        return idList;
    }

    @Override
    public List savePushMessageToDevices(Map<String, String> requestParams) {
        List<String> idList = new ArrayList<>();
        String deviceids = requestParams.get("deviceids");
        String strDeviceType = requestParams.get("device_type");
        String appkey = requestParams.get("appkey");
        final List<String> lstDeviceids = Json.fromJsonAsList(String.class, deviceids);
        final PushMessage thePushmsg = getPushMessage(requestParams);
        PushDeviceType deviceType = null;
        if (StringUtil.isNotEmpty(strDeviceType)) {
            deviceType = Enum.valueOf(PushDeviceType.class, strDeviceType.trim());
        }
        Map<String, Object> deviceConf = (Map<String, Object>) Mapl.cell(globalInfo.getPushConfigs(),
            appkey + ".device");
        if (deviceConf == null) {
            throw new PushException("appkey: " + appkey + " is not valid!msg:" + JSONUtil.toJSONIgnoreException(thePushmsg));
        }
        Map<String, Object> mapPushModes = (Map<String, Object>) deviceConf.get(deviceType.toString());
        for (String strPushMode : mapPushModes.keySet()) {
            PushMode pushMode = Enum.valueOf(PushMode.class, strPushMode.trim());
            AbstractPushVendorService pushService = pushVendorFactory.getPushVendorService(appkey, deviceType,
                pushMode);
            // 查找存在的deviceid对应的有效的设备
            if (CollectionUtil.isNotEmpty(lstDeviceids)) {
                List<PushDeviceEntity> lstDevices = deviceRepo.findByAppkeyAndDevicetypeAndPushModeAndMstatusAndDeviceidIn(appkey, deviceType,
                    pushMode, PushDeviceStatus.VALID, lstDeviceids);
                List<PushMsgEntity> list = doSavePushMsg(appkey, thePushmsg, deviceType, pushMode, lstDevices);
                list.forEach(n -> idList.add(n.getId()));
            } else {
                LOGGER.error("savePushMessageToDevices but the deviceids is empty!");
            }
        }
        return idList;
    }

    @Override
    public List savePushMessageToAllUsers(Map<String, String> requestParams) throws IOException {
        List<String> idList = new ArrayList<>();
        String appkey = requestParams.get("appkey");
        final int dbBatchSize = globalInfo.getDbBatchSize();
        Pageable pageable = new PageRequest(0, dbBatchSize);
        List<String> lstUids = new ArrayList<>(dbBatchSize);
        boolean hasData;
        do {
            Page<PushUserEntity> page = pushUserRepository.findByAppkey(appkey, pageable);
            pageable = pageable.next();

            hasData = page.hasContent();
            if (hasData) {
                List<PushUserEntity> lstUser = page.getContent();
                lstUids.clear();
                lstUser.forEach(userEntity -> lstUids.add(userEntity.getUserid()));
                requestParams.put("userids", JSONUtil.toJSON(lstUids).toString());
                List<String> list = savePushMessageToUsers(requestParams);
                idList.addAll(list);
            }
        } while (hasData);
        return idList;
    }

    @Override
    public List savePushMessageToAllDevices(Map<String, String> requestParams) throws IOException {
        List<String> idList = new ArrayList<>();

        String appkey = requestParams.get("appkey");
        String strDeviceType1 = requestParams.get("device_type");
        PushDeviceType deviceType0 = null;
        if (StringUtil.isNotEmpty(strDeviceType1)) {
            deviceType0 = Enum.valueOf(PushDeviceType.class, strDeviceType1.trim());
        }

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
            if (deviceType0 != null && deviceType != deviceType0) {
                continue;
            }
            Pageable pageable = new PageRequest(0, dbBatchSize);
            do {
                Page<PushDeviceEntity> page = deviceRepo.findByAppkeyAndDevicetypeAndMstatus(appkey, deviceType,
                    PushDeviceStatus.VALID, pageable);
                pageable = pageable.next();

                hasData = page.hasContent();
                if (hasData) {
                    List<PushDeviceEntity> lstDevice = page.getContent();
                    lstDids.clear();
                    lstDevice.forEach(deviceEntity -> lstDids.add(deviceEntity.getDeviceid()));
                    requestParams.put("device_type", deviceType.toString());
                    requestParams.put("deviceids", JSONUtil.toJSON(lstDids).toString());
                    List<String> list = savePushMessageToDevices(requestParams);
                    idList.addAll(list);
                }
            } while (hasData);
            // 传入的deviceType不为空，则只发送指定设备类型的消息
            if (deviceType0 != null) {
                break;
            }
        }
        return idList;
    }

    private List<PushMsgVO> convertVo(Page<PushMsgEntity> page) {
        List<PushMsgEntity> entityList = page.getContent();
        List<PushMsgVO> voList = new ArrayList<PushMsgVO>();
        for (PushMsgEntity entity : entityList) {
            PushMsgVO vo = new PushMsgVO();
            Object channelDesc = Mapl.cell(globalInfo.getPushConfigs(), entity.getAppkey() + ".desc");
            vo.setAppkey(channelDesc == null ? "" : channelDesc.toString());
            vo.setLastPushTime(entity.getLastModifyTime());
            vo.setMcontent(entity.getMcontent());
            vo.setMstatus(entity.getMstatus());
            DataDic dataDic = DataDicUtil.get("PEP_PUSH_CHANNEL_TYPE", entity.getPushMode().toString());
            vo.setPushMode(null == dataDic ? null : dataDic.getName());
            vo.setId(entity.getId());
            vo.setUserid(entity.getUserid());
            voList.add(vo);
        }
        return voList;
    }

    private PushMessage getPushMessage(Map<String, String> requestParams) {
        String smsg = requestParams.get("msg");
        final PushMessage thePushmsg;
        try {
            thePushmsg = JSONUtil.parse(smsg, PushMessage.class);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new PushException(e.getMessage());
        }
        return thePushmsg;
    }

    private List<PushMsgEntity> doSavePushMsg(String appkey, PushMessage thePushmsg, PushDeviceType deviceType, PushMode pushMode,
                                              List<PushDeviceEntity> lstDevices) {
        List<PushMsgEntity> saveResultList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(lstDevices)) {
            List<PushMsgEntity> lstMsgs = new ArrayList<PushMsgEntity>();
            for (PushDeviceEntity device : lstDevices) {
                if (StringUtil.isNotEmpty(device.getPushToken())) {
                    PushMsgEntity dbMsg = new PushMsgEntity(thePushmsg, appkey, device);
                    lstMsgs.add(dbMsg);
                    LOGGER.info("PushMsgServiceImpl doSavePushMsg pushId:{},pushEntity:{}",
                        dbMsg.getId(), JSONUtil.toJSONIgnoreException(dbMsg));
                } else {
                    LOGGER.info("PushMsgServiceImpl doSavePushMsg error: push token is empty:appkey:{},device:{},msg:{}"
                        + appkey, device.getDeviceid(), JSONUtil.toJSONIgnoreException(thePushmsg));
                }
            }
            try {
                saveResultList = pushMsgRepository.saveAll(lstMsgs);
            } catch (Exception e) {
                throw new PushException(e.getMessage());
            }
        } else {
            LOGGER.info("PushMsgServiceImpl doSavePushMsg info: device is empty,then no need send msg!");
        }
        return saveResultList;
    }
}
