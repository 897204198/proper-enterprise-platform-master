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

/**
 * 历史属性切面
 * 实体保存时自动添加或更新 创建人/时间 和 修改人/时间
 *
 * 切面配置在 spring/auth/common/applicationContext-auth-common.xml
 * 作用在 spring-data Repository 的 save 方法前
 */
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
        // Repository.save* 方法的参数只有两类
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
