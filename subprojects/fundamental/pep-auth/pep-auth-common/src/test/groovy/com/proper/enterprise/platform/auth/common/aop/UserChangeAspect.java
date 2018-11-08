package com.proper.enterprise.platform.auth.common.aop;

import com.proper.enterprise.platform.api.auth.model.User;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Aspect
@Component
public class UserChangeAspect {

    @AfterReturning(pointcut = "com.proper.enterprise.platform.auth.common.service.UserServiceAspect.save()", returning = "users")
    public void insert(User... users) {
        for (User user : users) {
            UserChangeAspectTest.CHANGE_MARK.put("insertA" + user.getId(), true);
        }
    }

    @AfterReturning(pointcut = "com.proper.enterprise.platform.auth.common.service.UserServiceAspect.update()", returning = "user")
    public void update(User user) {
        UserChangeAspectTest.CHANGE_MARK.put("updateA" + user.getName(), true);
    }

    @AfterReturning(pointcut = "com.proper.enterprise.platform.auth.common.service.UserServiceAspect.update()", returning = "users")
    public void updateBatch(Collection<? extends User> users) {
        for (User user : users) {
            UserChangeAspectTest.CHANGE_MARK.put("updateA" + user.getName(), true);
        }
    }

    @After("com.proper.enterprise.platform.auth.common.service.UserServiceAspect.delete() || "
        + "com.proper.enterprise.platform.auth.common.service.UserServiceAspect.batchDelete()")
    public void delete(JoinPoint joinPoint) {
        for (Object id : joinPoint.getArgs()) {
            UserChangeAspectTest.CHANGE_MARK.put("deleteA" + id, true);
        }
    }

}
