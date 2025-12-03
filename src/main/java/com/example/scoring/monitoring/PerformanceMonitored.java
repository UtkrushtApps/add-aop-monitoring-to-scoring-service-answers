package com.example.scoring.monitoring;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation indicating that a method (or all methods on a type) should have
 * its execution time measured and logged by the monitoring aspect.
 *
 * This provides an opt-in mechanism for performance-sensitive operations.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PerformanceMonitored {
}
