package com.distro.device_service.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class ControllerAspect {

   @Pointcut("execution(com.distro.device_service.controller.*.*(..)")
   public void controllerMethods(){}

    @Before("controllerMethods()")
    public void beforeController(JoinPoint joinPoint){
        log.info("Controller " + joinPoint.getSignature().getName() + "calledd" + joinPoint.getTarget());
    }


}
