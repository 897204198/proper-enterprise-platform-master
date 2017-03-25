package com.proper.enterprise.platform.push.common.aspect;

import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.proper.enterprise.platform.core.api.IBase;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;

/**
 * 推送模块历史属性切面，用户采用默认用户 实体保存时自动添加或更新 创建人/时间 和 修改人/时间
 *
 * 切面配置在 spring/push/common/applicationContext-auth-common.xml 作用在 spring-data
 * Repository 的 save 方法前
 */
public class PushHistoricalAdvice {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushHistoricalAdvice.class);

    private static final String DEFAULT_USER_ID = "PEP_SYS";

    public void beforeSave(JoinPoint jp) {
        LOGGER.trace("HistoricalAdvice before {} with {} args.", jp.getSignature().getName(), jp.getArgs());

        String userId = DEFAULT_USER_ID;

        Object obj = jp.getArgs()[0];
        if (obj instanceof IBase) {
            update((IBase) obj, userId);
        } else if (obj instanceof Iterable) {
            for (Object entity : (Iterable) obj) {
                update((IBase) entity, userId);
            }
        }
    }

    private void update(IBase entity, String userId) {
        if (StringUtil.isNull(entity.getId())) {
            entity.setCreateUserId(userId);
            entity.setCreateTime(DateUtil.getTimestamp());
        }
        entity.setLastModifyUserId(userId);
        entity.setLastModifyTime(DateUtil.getTimestamp());
    }
}
