package com.proper.enterprise.platform.core.interceptor;

import com.proper.enterprise.platform.core.convert.annotation.POJORelevance;
import com.proper.enterprise.platform.core.pojo.BaseVO;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * 根据定义的规范
 * service层中传入的VO统一转成VO对应的DO
 */

public class ServiceConvertInterceptor {

    public Object serviceBeforeInterceptor(ProceedingJoinPoint pjp) throws Throwable {
        if (0 == pjp.getArgs().length) {
            return pjp.proceed();
        }
        return pjp.proceed(handleParam(pjp.getArgs()));
    }

    private Object[] handleParam(Object[] params) {
        List handleParams = new ArrayList();
        for (Object param : params) {
            if (param instanceof BaseVO) {
                POJORelevance pojoRelevance = param.getClass().getAnnotation(POJORelevance.class);
                if (null == pojoRelevance) {
                    handleParams.add(param);
                    continue;
                }
                Class relevanceDO = null;
                if (StringUtils.isNotEmpty(pojoRelevance.relevanceDOClassName())) {
                    relevanceDO = BeanUtil.getClassType(pojoRelevance.relevanceDOClassName());
                    if (null != relevanceDO) {
                        handleParams.add(BeanUtil.convertToDO(param, relevanceDO));
                        continue;
                    }
                }
                if (pojoRelevance.relevanceDO() == void.class) {
                    handleParams.add(param);
                    continue;
                }
                relevanceDO = pojoRelevance.relevanceDO();
                handleParams.add(BeanUtil.convertToDO(param, relevanceDO));
                continue;
            }
            handleParams.add(param);
        }
        return handleParams.toArray();
    }
}
