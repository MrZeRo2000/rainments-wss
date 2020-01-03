package com.romanpulov.rainmentswss.aspect;

import org.aspectj.lang.annotation.Pointcut;

public class JointPointConfig {

    @Pointcut("execution(* com.romanpulov.rainmentswss.controller..*(..))")
    public void controllerExecution() {

    }

    @Pointcut("execution(* com.romanpulov.rainmentswss.repository..*(..))")
    public void repositoryExecution() {

    }
}
