package com.proper.enterprise.platform.auth.common.aop;

import com.proper.enterprise.platform.api.auth.model.User;
import org.aspectj.lang.JoinPoint;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component("userChangeAspect")
@Lazy(false)
public class UserChangeAspect {

    public void insert(User... users) {
        for (User user : users) {
            UserChangeAspectTest.CHANGE_MARK.put("insertA" + user.getId(), true);
        }
    }

    public void update(User user) {
        UserChangeAspectTest.CHANGE_MARK.put("updateA" + user.getName(), true);
    }

    public void updateBatch(Collection<? extends User> users) {
        for (User user : users) {
            UserChangeAspectTest.CHANGE_MARK.put("updateA" + user.getName(), true);
        }
    }

    public void delete(JoinPoint joinPoint) {
        for (Object id : joinPoint.getArgs()) {
            UserChangeAspectTest.CHANGE_MARK.put("deleteA" + id, true);
        }
    }

}
