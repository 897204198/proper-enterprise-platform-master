package com.proper.enterprise.platform.core.mongo;

import com.proper.enterprise.platform.core.api.IBase;
import com.proper.enterprise.platform.core.mongo.document.BaseDocument;
import com.proper.enterprise.platform.core.security.Authentication;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 历史属性切面，用来给mongo数据提供历史信息
 * 实体保存时自动添加或更新 创建人/时间 和 修改人/时间
 * 作用在 spring-data Repository 的 save 方法前，仅针对 BaseDocument 的子类生效
 */
@Aspect
@Component
public class HistoricalAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(HistoricalAspect.class);

    @Pointcut("execution(public * org.springframework.data.repository.Repository+.save*(..))")
    public void savePointcut() {
    }


    @Before("savePointcut()")
    public void beforeSave(JoinPoint jp) {
        LOGGER.trace("HistoricalAdvice before {} with {} args.", jp.getSignature().getName(), jp.getArgs());

        Object obj = jp.getArgs()[0];
        // Repository.save* 方法的参数只有两类
        if (obj instanceof Iterable) {
            for (Object o : (Iterable) obj) {
                update(o, Authentication.getCurrentUserId());
            }
        } else {
            update(obj, Authentication.getCurrentUserId());
        }
    }

    private void update(Object obj, String userId) {
        if (obj instanceof BaseDocument) {
            IBase ibase = (IBase) obj;
            if (StringUtil.isNull(ibase.getId()) || StringUtil.isNull(ibase.getCreateUserId())) {
                if (null == ibase.getEnable()) {
                    ibase.setEnable(true);
                }
                ibase.setCreateUserId(userId);
                ibase.setCreateTime(DateUtil.getTimestamp(true));
            }
            ibase.setLastModifyUserId(userId);
            ibase.setLastModifyTime(DateUtil.getTimestamp(true));
        }
    }
}
