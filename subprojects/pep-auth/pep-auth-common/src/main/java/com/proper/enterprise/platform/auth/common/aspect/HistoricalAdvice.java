package com.proper.enterprise.platform.auth.common.aspect;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.core.api.IBase;
import com.proper.enterprise.platform.core.utils.ConfCenter;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
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

    private static final String DEFAULT_USER_ID = ConfCenter.get("auth.historical.defaultUserId", "PEP_SYS");

    @Autowired(required = false)
    UserService userService;

    public void beforeSave(JoinPoint jp) throws Exception {
        LOGGER.trace("HistoricalAdvice before {} with {} args.", jp.getSignature().getName(), jp.getArgs());

        String userId = DEFAULT_USER_ID;
        try {
            User user = userService.getCurrentUser();
            LOGGER.trace("Current user is {}({})", user.getUsername(), user.getId());
            userId = user.getId();
        } catch (Exception e) {
            LOGGER.debug("Get current user throws exception: {}, fall back to use default user id.", e.getMessage());
        }

        Object obj = jp.getArgs()[0];
        // Repository.save* 方法的参数只有两类
        if (obj instanceof Iterable) {
            for (Object o : (Iterable) obj) {
                update(o, userId);
            }
        } else {
            update(obj, userId);
        }
    }

    private void update(Object obj, String userId) {
        if (obj instanceof IBase) {
            IBase ibase = (IBase) obj;
            if (StringUtil.isNull(ibase.getId())) {
                ibase.setCreateUserId(userId);
                ibase.setCreateTime(DateUtil.getTimestamp(true));
            }
            ibase.setLastModifyUserId(userId);
            ibase.setLastModifyTime(DateUtil.getTimestamp(true));
        } else {
            LOGGER.debug("{} is not IBase, no need to add historical info.", JSONUtil.toJSONIgnoreException(obj));
        }
    }
}
