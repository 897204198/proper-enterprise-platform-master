package com.proper.enterprise.platform.core.jpa.handler;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("jpaExceptionHandler")
public class JpaExceptionHandler {

    @Autowired
    private ConstraintViolationExceptionHandler constraintViolationExceptionHandler;

    public void handle(Throwable ex) {
        if (ex.getCause() instanceof ConstraintViolationException) {
            ConstraintViolationException throwable = (ConstraintViolationException) ex.getCause();
            constraintViolationExceptionHandler.handle(throwable);
        }
    }


}
