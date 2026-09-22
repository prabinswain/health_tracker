package com.distro.user_service.aop;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@Aspect
public class ExecutionCalculateAspect {

    @Pointcut("execution(* com.distro.user_service.controller.*.*(..))")
    public void controllerMethods(){}

    @Around("controllerMethods()")
    public Object measureExecutionTime(ProceedingJoinPoint jnp){

        long start = System.nanoTime();
        try{
           return jnp.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }finally {
            long elapseNs = System.nanoTime() - start;
            long elapseMs = TimeUnit.MILLISECONDS.toMillis(elapseNs);
            log.info("Controller method : {} execution time : {}ms", jnp.getSignature().toShortString() , elapseMs );
        }

    }

}
