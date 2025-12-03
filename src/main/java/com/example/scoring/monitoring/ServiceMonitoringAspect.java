package com.example.scoring.monitoring;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Cross-cutting monitoring aspect that provides:
 * <ul>
 *     <li>Structured entry and exit logging for all service-layer methods.</li>
 *     <li>Execution time measurement for methods annotated with {@link PerformanceMonitored}.</li>
 * </ul>
 *
 * The aspect is deliberately focused on the service layer so that controllers and
 * domain objects remain free of cross-cutting concerns.
 */
@Aspect
@Component
public class ServiceMonitoringAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceMonitoringAspect.class);

    private static final int MAX_ARGUMENT_LOG_LENGTH = 500;

    /**
     * Pointcut targeting all public methods on types in the service package.
     */
    @Pointcut("execution(public * com.example.scoring.service..*(..))")
    public void anyServiceOperation() {
        // Pointcut definition
    }

    /**
     * Around advice responsible for logging method entry/exit and, when requested
     * via {@link PerformanceMonitored}, capturing execution time.
     */
    @Around("anyServiceOperation()")
    public Object logAndTime(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        boolean performanceMonitored = isPerformanceMonitored(method, signature.getDeclaringType());

        String methodName = signature.toShortString();
        String argumentsAsString = buildArgumentsString(joinPoint.getArgs());

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("[SERVICE-ENTRY] method={} args={}", methodName, argumentsAsString);
        }

        long startTimeNanos = performanceMonitored ? System.nanoTime() : 0L;

        try {
            Object result = joinPoint.proceed();

            if (performanceMonitored) {
                long durationMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTimeNanos);
                LOGGER.info("[SERVICE-EXIT] method={} durationMs={} resultType={}",
                        methodName, durationMillis, resultType(result));
            } else {
                LOGGER.info("[SERVICE-EXIT] method={} resultType={} (timing disabled)",
                        methodName, resultType(result));
            }

            return result;
        } catch (Throwable ex) {
            if (performanceMonitored && startTimeNanos > 0L) {
                long durationMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTimeNanos);
                LOGGER.error("[SERVICE-ERROR] method={} durationMs={} message={}",
                        methodName, durationMillis, ex.getMessage(), ex);
            } else {
                LOGGER.error("[SERVICE-ERROR] method={} message={}", methodName, ex.getMessage(), ex);
            }
            throw ex;
        }
    }

    private boolean isPerformanceMonitored(Method method, Class<?> declaringType) {
        PerformanceMonitored byMethod = AnnotationUtils.findAnnotation(method, PerformanceMonitored.class);
        if (byMethod != null) {
            return true;
        }
        PerformanceMonitored byType = AnnotationUtils.findAnnotation(declaringType, PerformanceMonitored.class);
        return byType != null;
    }

    private String buildArgumentsString(Object[] args) {
        if (args == null || args.length == 0) {
            return "[]";
        }
        String joined = Arrays.deepToString(args);
        if (joined.length() > MAX_ARGUMENT_LOG_LENGTH) {
            return joined.substring(0, MAX_ARGUMENT_LOG_LENGTH) + "... (truncated)";
        }
        return joined;
    }

    private String resultType(Object result) {
        return result == null ? "null" : result.getClass().getSimpleName();
    }
}
