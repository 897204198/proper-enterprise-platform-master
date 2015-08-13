package com.proper.enterprise.platform.auth.aspect;

import org.aspectj.lang.JoinPoint;

public class JpaRepositoryAspect {
    
    public void beforeSave(JoinPoint joinPoint) {
        Object[] objs = joinPoint.getArgs();
        
        System.out.println(" ===== HINEX ===== ");
        for (Object obj : objs) {
            System.out.println(obj.getClass().getCanonicalName());
        }
        System.out.println(" ================= ");
    }

}
