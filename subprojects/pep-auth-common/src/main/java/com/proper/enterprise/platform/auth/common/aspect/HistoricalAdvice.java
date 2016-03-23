package com.proper.enterprise.platform.auth.common.aspect;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.core.conf.ConfManager;
import com.proper.enterprise.platform.core.entity.BaseEntity;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class HistoricalAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(HistoricalAdvice.class);

    private static final String DEFAULT_USER_ID = ConfManager.getString("auth.common", "historical.defaultUserId", "PEP_SYS");

    private static final String DEFAULT_TENANT_ID = ConfManager.getString("auth.common", "historical.defaultTenantId", "PEP");

    @Autowired
    UserService userService;

    public void beforeSave(JoinPoint jp) {
        LOGGER.trace("HistoricalAdvice before {} with {} args.", jp.getSignature().getName(), jp.getArgs());

        String userId = DEFAULT_USER_ID;
        String tenantId = DEFAULT_TENANT_ID;
        try {
            User user = userService.getCurrentUser();
            LOGGER.trace("Current user is {}({})", user.getUsername(), user.getId());
            userId = user.getId();
            tenantId = user.getTenantId();
        } catch (Exception e) {
            LOGGER.debug("Get current user throws exception {}", e.getMessage());
        }

        Object obj = jp.getArgs()[0];
        if (obj instanceof BaseEntity) {
            update((BaseEntity) obj, userId, tenantId);
        } else if (obj instanceof Iterable) {
            for (Object entity : (Iterable) obj) {
                update((BaseEntity)entity, userId, tenantId);
            }
        }
    }

    private void update(BaseEntity entity, String userId, String tenantId) {
        if (StringUtil.isNull(entity.getId())) {
            entity.setCreateUserId(userId);
            entity.setCreateTime(DateUtil.getCurrentDateString());
            entity.setTenantId(tenantId);
        }
        entity.setLastModifyUserId(userId);
        entity.setLastModifyTime(DateUtil.getCurrentDateString());
    }
}
