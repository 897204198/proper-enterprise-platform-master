package com.proper.enterprise.platform.auth.aspect;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.auth.service.APISecret;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component("jwtAuthAspect")
@Lazy(false)
public class JWTAuthAspect {

    @Autowired
    private APISecret secret;


    public void update(User user) {
        if (!user.getEnable()) {
            secret.clearAPISecret(user.getId());
        }
    }

    public void updateBatch(Collection<? extends User> users) {
        for (User user : users) {
            secret.clearAPISecret(user.getId());
        }
    }

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
