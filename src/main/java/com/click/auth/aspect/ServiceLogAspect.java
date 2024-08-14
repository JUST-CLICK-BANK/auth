package com.click.auth.aspect;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class ServiceLogAspect {

    @Before("service()")
    public void beforeServiceRequest(JoinPoint joinPoint) {
        log.info("##### Call - {}", joinPoint.getSignature().toShortString());
        Arrays.stream(joinPoint.getArgs())
            .map(Object::toString)
            .map(str -> str + "\t")
            .forEach(str -> log.info("\t- {}", str));
    }

    @AfterReturning(pointcut = "service()", returning = "returnValue")
    public void afterServiceReturning(JoinPoint joinPoint, Object returnValue) {
        log.info("##### Return - {}", joinPoint.getSignature().toShortString());
        if (returnValue != null) {
            log.info("\t- {}", returnValue.toString());
        }
    }

    @AfterThrowing(pointcut = "service()", throwing = "e")
    public void afterServiceThrowing(JoinPoint joinPoint, Exception e) {
        log.error("##### Error in - {}", joinPoint.getSignature().toShortString());
        log.error("\t{}", e.getMessage());
    }

    @Pointcut("within(com.click.auth.service.*)")
    public void service() {
    }

}
