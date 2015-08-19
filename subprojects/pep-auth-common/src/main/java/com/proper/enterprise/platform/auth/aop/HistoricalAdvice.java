package com.proper.enterprise.platform.auth.aop;

import com.proper.enterprise.platform.core.entity.BaseEntity;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class HistoricalAdvice implements MethodBeforeAdvice {

    private final static Logger LOGGER = LoggerFactory.getLogger(HistoricalAdvice.class);

    public static String USER_ID = "aop";

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        LOGGER.trace("HistoricalAdvice before {} with {} args.", method, args.length);
        Object obj = args[0];
        if (obj instanceof BaseEntity) {
            update((BaseEntity) obj);
        } else if (obj instanceof Iterable) {
            for (Object entity : (Iterable) obj) {
                update((BaseEntity)entity);
            }
        }
    }

    private void update(BaseEntity entity) {
        if (StringUtil.isNull(entity.getId())) {
            entity.setCreateUserId(USER_ID);
            entity.setCreateTime(DateUtil.getCurrentDateString());
        }
        entity.setLastModifyUserId(USER_ID);
        entity.setLastModifyTime(DateUtil.getCurrentDateString());
    }
}
