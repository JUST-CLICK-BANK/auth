package com.click.auth.aspect;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class ControllerLogAspect {

    @Around("controller()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        log.info("### {} seconds in {}", (double)(endTime - startTime)/1000, joinPoint.getSignature().toShortString());
        return proceed;
    }

    @Before("controller()")
    public void beforeControllerRequest(JoinPoint joinPoint) {
        log.info("### Start - {}", joinPoint.getSignature().toShortString());
        Arrays.stream(joinPoint.getArgs())
            .map(Object::toString)
            .map(str -> str + "\t")
            .forEach(str -> log.info("\t- {}", str));
    }

    @AfterReturning(pointcut = "controller()", returning = "returnValue")
    public void afterControllerReturning(JoinPoint joinPoint, Object returnValue) {
        log.info("### End - {}", joinPoint.getSignature().toShortString());
        if (returnValue != null) {
            log.info("\t- {}", returnValue.toString());
        }
    }

    @Pointcut("within(com.click.auth.controller.*)")
    public void controller() {
    }

}
