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

    private static final String DEFAULT_USER_ID = ConfManager.getString("auth.common", "historical.defaultUserId", "SYS");

    @Autowired
    UserService userService;

    public void beforeSave(JoinPoint jp) {
        LOGGER.trace("HistoricalAdvice before {} with {} args.", jp.getSignature().getName(), jp.getArgs());

        String userId = DEFAULT_USER_ID;
        try {
            User user = userService.getCurrentUser();
            LOGGER.trace("Current user is {}({})", user.getUsername(), user.getId());
            userId = user.getId();
        } catch (Exception e) {
            LOGGER.debug("Get current user throws exception {}", e.getMessage());
        }

        Object obj = jp.getArgs()[0];
        if (obj instanceof BaseEntity) {
            update((BaseEntity) obj, userId);
        } else if (obj instanceof Iterable) {
            for (Object entity : (Iterable) obj) {
                update((BaseEntity)entity, userId);
            }
        }
    }

    private void update(BaseEntity entity, String userId) {
        if (StringUtil.isNull(entity.getId())) {
            entity.setCreateUserId(userId);
            entity.setCreateTime(DateUtil.getCurrentDateString());
        }
        entity.setLastModifyUserId(userId);
        entity.setLastModifyTime(DateUtil.getCurrentDateString());
    }
}
