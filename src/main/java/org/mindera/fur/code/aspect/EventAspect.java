package org.mindera.fur.code.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class EventAspect {

    @Before("execution(* com.ruitx.formation.controller.*.*(..))")
    public void logGenericActionToDB(JoinPoint joinPoint) {

    }
}
