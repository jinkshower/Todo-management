package com.spring.todomanagement.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;

@Order(Ordered.LOWEST_PRECEDENCE - 1)
@Aspect
@Component
public class OptimisticLockRetryAspect {

    private static final int MAX_RETRIES = 1000;
    private static final long RETRY_DELAY_MS = 100;

    @Around("@annotation(com.spring.todomanagement.common.aspect.Retry)")
    public Object retryOptimisticLock(ProceedingJoinPoint joinPoint) throws Throwable {
        int retryCount = 0;
        Throwable lastException = null;

        while (retryCount < MAX_RETRIES) {
            try {
                return joinPoint.proceed();
            } catch (OptimisticLockingFailureException e) {
                lastException = e;
                retryCount++;
                Thread.sleep(RETRY_DELAY_MS);
            }
        }
        throw lastException;
    }
}
