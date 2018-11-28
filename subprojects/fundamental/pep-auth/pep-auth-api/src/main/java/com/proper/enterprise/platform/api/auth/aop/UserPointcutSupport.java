package com.proper.enterprise.platform.api.auth.aop;

import org.aspectj.lang.annotation.Pointcut;

public class UserPointcutSupport {

    @Pointcut("execution(* com.proper..service.UserService.save(..))")
    public void userSavePointcut() {}

    @Pointcut("execution(* com.proper..service.UserService.update*(..))")
    public void userUpdatePointcut() {}

    @Pointcut("execution(* com.proper..service.UserService.delete(..))")
    public void userDeletePointcut() {}

    @Pointcut("execution(* com.proper..service.UserService.deleteByIds(..))")
    public void userDeleteBatchPointcut() {}
}
