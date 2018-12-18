package com.proper.enterprise.platform.auth.aspect;

import com.proper.enterprise.platform.api.auth.aop.UserPointcutSupport;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.auth.service.APISecret;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Aspect
@Component
@Lazy(false)
public class JWTAuthAspect extends UserPointcutSupport {

    @Autowired
    private APISecret secret;

    @AfterReturning(pointcut = "userUpdatePointcut()",
        returning = "user")
    public void update(User user) {
        if (!user.getEnable()) {
            secret.clearAPISecret(user.getId());
        }
    }

    @AfterReturning(pointcut = "userUpdatePointcut()",
        returning = "users")
    public void updateBatch(Collection<? extends User> users) {
        for (User user : users) {
            secret.clearAPISecret(user.getId());
        }
    }

    @AfterReturning(pointcut = "userDeletePointcut() || userDeleteBatchPointcut()")
    public void delete(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String ids = (String) args[0];
        if (StringUtil.isEmpty(ids)) {
            return;
        }
        String[] idsAttr = ids.split(",");
        for (String id : idsAttr) {
            secret.clearAPISecret(id);
        }
    }
}
