package com.proper.enterprise.platform.push.common.db.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.push.common.db.entity.PushDeviceEntity;
import com.proper.enterprise.platform.push.common.model.enums.PushDeviceStatus;
import com.proper.enterprise.platform.push.common.model.enums.PushDeviceType;
import com.proper.enterprise.platform.push.common.model.enums.PushMode;

public interface PushDeviceRepository extends BaseRepository<PushDeviceEntity, String> {

    PushDeviceEntity findByAppkeyAndDeviceid(String appkey, String deviceid);

    Page<PushDeviceEntity> findByAppkeyAndDevicetypeAndMstatus(String appkey, PushDeviceType devicetype,
            PushDeviceStatus mstatus, Pageable pageable);

    List<PushDeviceEntity> findByAppkeyAndDevicetypeAndPushModeAndMstatusAndUseridIn(String appkey,
            PushDeviceType devicetype, PushMode pushMode, PushDeviceStatus mstatus, Collection<String> lstUids);

    List<PushDeviceEntity> findByAppkeyAndDevicetypeAndPushModeAndMstatusAndDeviceidIn(String appkey,
            PushDeviceType deviceType, PushMode pushMode, PushDeviceStatus valid, List<String> lstDeviceids);

    List<PushDeviceEntity> findByAppkeyAndUserid(String appkey, String userid);
}
