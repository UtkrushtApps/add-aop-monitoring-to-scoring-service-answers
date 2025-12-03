package com.example.scoring.monitoring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Central configuration for monitoring concerns. Enabling AspectJ auto proxy here
 * keeps the setup explicit and makes it easy to extend with additional monitoring
 * mechanisms (e.g. tracing, metrics) in the future.
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class MonitoringConfiguration {
    // Additional monitoring beans or configuration can be placed here.
}
