package com.proper.enterprise.platform.auth.aop;

import com.proper.enterprise.platform.core.entity.BaseEntity;
import com.proper.enterprise.platform.core.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Iterator;

@Component
public class HistoricalAdvice implements MethodBeforeAdvice {

    private final static Logger LOGGER = LoggerFactory.getLogger(HistoricalAdvice.class);

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        LOGGER.trace("HistoricalAdvice before {} with {} args.", method, args.length);
        Object obj = args[0];
        if (obj instanceof BaseEntity) {
            update((BaseEntity) obj);
        } else if (obj instanceof Iterable) {
            Iterator<BaseEntity> iter = ((Iterable) obj).iterator();
            while (iter.hasNext()) {
                update(iter.next());
            }
        }
    }

    private void update(BaseEntity entity) {
        entity.setLastModifyUserId("aop");
        entity.setLastModifyTime(DateUtil.getCurrentDateString());
    }
}
