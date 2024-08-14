package com.click.auth.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.Joinpoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LogAspect {

    @Before("controller()")
    public void beforeControllerRequest(Joinpoint joinpoint) {
        log.info("");
    }

    @AfterReturning(pointcut = "controller()", returning = "returnValue")
    public void afterControllerReturning() {
        log.info("");
    }

    @AfterThrowing(pointcut = "controller()", throwing = "e")
    public void afterControllerThrowing(Joinpoint joinpoint, Exception e) {
        log.error("");
    }

    @Pointcut("within(com.click.auth.controller.*)")
    public void controller() {
    }

}
