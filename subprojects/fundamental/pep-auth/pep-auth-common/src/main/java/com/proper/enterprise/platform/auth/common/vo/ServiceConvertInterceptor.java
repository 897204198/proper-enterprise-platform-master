package com.proper.enterprise.platform.auth.common.vo;

import com.proper.enterprise.platform.core.pojo.BaseVO;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 根据定义的规范
 * service层中传入的VO统一转成VO对应的DO
 */
@Aspect
@Component
public class ServiceConvertInterceptor {

    @Around("execution(* com.proper..service.impl.*ServiceImpl.*(..)) || execution(* com.proper..service.impl.*ServiceSupport.*(..))")
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
                        handleParams.add(BeanUtil.convert(param, relevanceDO));
                        continue;
                    }
                }
                if (pojoRelevance.relevanceDO() == void.class) {
                    handleParams.add(param);
                    continue;
                }
                relevanceDO = pojoRelevance.relevanceDO();
                handleParams.add(BeanUtil.convert(param, relevanceDO));
                continue;
            }
            handleParams.add(param);
        }
        return handleParams.toArray();
    }
}
