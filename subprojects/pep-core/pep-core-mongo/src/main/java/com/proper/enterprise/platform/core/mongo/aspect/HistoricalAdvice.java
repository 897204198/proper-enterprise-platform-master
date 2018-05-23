package com.proper.enterprise.platform.core.mongo.aspect;

import com.proper.enterprise.platform.core.api.IBase;
import com.proper.enterprise.platform.core.mongo.document.BaseDocument;
import com.proper.enterprise.platform.core.security.util.SecurityUtil;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 历史属性切面  用来给mongo提供历史切面
 * 实体保存时自动添加或更新 创建人/时间 和 修改人/时间
 * <p>
 * 切面配置在 spring/core/mongo/applicationContext-core-mongo.xml
 * 作用在 spring-data-mongo Repository 的 save 方法前 且类型是BaseDocument
 */
public class HistoricalAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(HistoricalAdvice.class);

    public void beforeSave(JoinPoint jp) {
        LOGGER.trace("HistoricalAdvice before {} with {} args.", jp.getSignature().getName(), jp.getArgs());

        Object obj = jp.getArgs()[0];
        // Repository.save* 方法的参数只有两类
        if (obj instanceof Iterable) {
            for (Object o : (Iterable) obj) {
                update(o, SecurityUtil.getCurrentUserId());
            }
        } else {
            update(obj, SecurityUtil.getCurrentUserId());
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
