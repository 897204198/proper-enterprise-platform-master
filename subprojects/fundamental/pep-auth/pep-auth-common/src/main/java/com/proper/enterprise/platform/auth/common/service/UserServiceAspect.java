package com.proper.enterprise.platform.auth.common.service;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class UserServiceAspect {

    @Pointcut("execution(* com.proper..service.UserService.save(..))")
    public void save() {}

    @Pointcut("execution(* com.proper..service.UserService.update*(..))")
    public void update() {}

    @Pointcut("execution(* com.proper..service.UserService.delete(..))")
    public void delete() {}

    @Pointcut("execution(* com.proper..service.UserService.deleteByIds(..))")
    public void batchDelete() {}

}
