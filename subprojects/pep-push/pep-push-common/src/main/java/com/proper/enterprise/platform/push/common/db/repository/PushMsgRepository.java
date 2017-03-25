package com.proper.enterprise.platform.push.common.db.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.push.common.db.entity.PushDeviceEntity;
import com.proper.enterprise.platform.push.common.db.entity.PushMsgEntity;
import com.proper.enterprise.platform.push.common.model.enums.PushMode;
import com.proper.enterprise.platform.push.common.model.enums.PushMsgStatus;

public interface PushMsgRepository extends BaseRepository<PushMsgEntity, String> {

    List<PushMsgEntity> findByAppkeyAndMsgidIn(String appkey, List<String> lstMsgids);

    List<PushMsgEntity> findByAppkeyAndDeviceAndUseridAndMstatus(String appkey, PushDeviceEntity device, String uid,
            PushMsgStatus unsend);

    Page<PushMsgEntity> findByAppkeyAndMstatusAndSendCountLessThanEqualAndDevicePushMode(String appkey,
            PushMsgStatus unsend, int maxSendCount, PushMode pushMode, Pageable pageable);

    @Modifying
    @Query("delete from PushMsgEntity m where m.appkey=:appkey and  m.createTime <= :date2")
    public int deleteByAppkeyAndCreateTimeLessThan(@Param("appkey") String appkey, @Param("date2") String date2);

}
