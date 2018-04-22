package com.proper.enterprise.platform.core.listener;

import com.proper.enterprise.platform.core.api.IBase;
import com.proper.enterprise.platform.core.security.util.SecurityUtil;
import com.proper.enterprise.platform.core.utils.DateUtil;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * 历史属性切面
 * 实体保存时自动添加或更新 创建人/时间 和 修改人/时间
 */
public class HistoricalEntityListener {

    @PrePersist
    public void prePersist(IBase ibase) {
        insert(ibase, SecurityUtil.getCurrentUserId());
    }

    @PreUpdate
    public void preUpdate(IBase ibase) {
        update(ibase, SecurityUtil.getCurrentUserId());
    }

    private void insert(IBase ibase, String userId) {
        ibase.setCreateUserId(userId);
        ibase.setCreateTime(DateUtil.getTimestamp(true));
        ibase.setLastModifyUserId(userId);
        ibase.setLastModifyTime(DateUtil.getTimestamp(true));
    }

    private void update(IBase ibase, String userId) {
        ibase.setLastModifyUserId(userId);
        ibase.setLastModifyTime(DateUtil.getTimestamp(true));
    }
}
