package com.distro.user_service.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Aspect
public class LoggingAspect {

    @Pointcut("execution(* com.distro.user_service.service.*.*(..))")
    public void serviceMethod(){}

    @Before("serviceMethod()")
    public void logBefore(JoinPoint joinPoint){
        log.info("Called service method: {} with arguments {}",
                joinPoint.getSignature().getName() , joinPoint.getArgs());
    }

    @AfterReturning("serviceMethod()")
    public void afterReturning(JoinPoint joinPoint , Object result ){
        log.info("Service method: {} returned {}",
                joinPoint.getSignature().getName() ,result);
    }


}
