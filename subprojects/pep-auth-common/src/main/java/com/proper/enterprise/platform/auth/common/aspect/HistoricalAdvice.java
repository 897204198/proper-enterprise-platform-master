package com.proper.enterprise.platform.auth.common.aspect;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.core.entity.BaseEntity;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class HistoricalAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(HistoricalAdvice.class);

    @Autowired
    UserService userService;

    public void beforeSave(JoinPoint jp) throws Exception {
        LOGGER.trace("HistoricalAdvice before {} with {} args.", jp.getSignature().getName(), jp.getArgs());

        User user = userService.getCurrentUser();
        LOGGER.trace("Current user is {}({})", user.getUsername(), user.getId());
        String userId = user.getId();

        Object obj = jp.getArgs()[0];
        if (obj instanceof Iterable) {
            for (Object entity : (Iterable) obj) {
                update((BaseEntity)entity, userId);
            }
        } else {
            update((BaseEntity) obj, userId);
        }
    }

    private void update(BaseEntity entity, String userId) {
        if (StringUtil.isNull(entity.getId())) {
            entity.setCreateUserId(userId);
            entity.setCreateTime(DateUtil.getTimestamp(true));
        }
        entity.setLastModifyUserId(userId);
        entity.setLastModifyTime(DateUtil.getTimestamp(true));
    }
}
