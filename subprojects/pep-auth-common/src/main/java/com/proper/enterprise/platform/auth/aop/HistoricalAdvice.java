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

//    @Autowired
//    UserService userService;

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        LOGGER.trace("HistoricalAdvice before {} with {} args.", method, args.length);

//        User user = userService.getCurrentUser();
//        LOGGER.trace("Current user is {}({})", user.getUsername(), user.getId());

        Object obj = args[0];
        if (obj instanceof BaseEntity) {
            update((BaseEntity) obj, "admin");
        } else if (obj instanceof Iterable) {
            for (Object entity : (Iterable) obj) {
                update((BaseEntity)entity, "admin");
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
