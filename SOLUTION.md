# Solution Steps

1. Create the Spring Boot application entry point `ScoringServiceApplication` and enable async support with `@EnableAsync` so that asynchronous service methods can run on separate threads.

2. Define the JPA entity `Score` with fields for ID, candidate ID, score value, question counts, calculation time, and a flag indicating whether the score was calculated asynchronously; annotate it with `@Entity` and map it to a `scores` table.

3. Create the `ScoreRepository` interface extending `JpaRepository<Score, Long>` so that the service layer can persist and load scores without boilerplate DAO code.

4. Introduce DTO classes: `ScoreRequest` (with validation annotations for candidateId, totalQuestions, correctAnswers), `ScoreResponse` (with a static `fromEntity` factory to map from the `Score` entity), and `ScoreAsyncResponse` (to represent the response for async score triggers).

5. Define the `ScoreService` interface with two methods: `calculateScoreSync(ScoreRequest)` for synchronous calculations and `calculateScoreAsync(ScoreRequest)` returning `CompletableFuture<Score>` for asynchronous processing.

6. Implement `ScoreServiceImpl` as a `@Service` that depends on `ScoreRepository`. In it, add a synchronous method annotated with `@Transactional` and `@PerformanceMonitored` that delegates to a shared core method, and an `@Async` asynchronous method that delegates to the same core logic and wraps the result in a `CompletableFuture`.

7. Implement the shared core method `performCalculationAndPersist` in `ScoreServiceImpl`, annotate it with `@Transactional` and `@PerformanceMonitored`, validate the request (including guarding against `correctAnswers > totalQuestions`), compute a simple percentage score, build a `Score` entity, persist it via `ScoreRepository`, and log a concise business-level info message.

8. Create a domain-specific runtime exception `ScoreCalculationException` and use it in `ScoreServiceImpl` to signal validation and persistence errors, wrapping unexpected runtime exceptions with clear messages and preserving the cause.

9. Implement the REST controller `ScoreController` under `/api/scores` with two endpoints: `POST /sync` that calls `scoreService.calculateScoreSync` and returns a `ScoreResponse`, and `POST /async` that calls `scoreService.calculateScoreAsync` in a fire-and-forget style and returns an HTTP 202 Accepted with a `ScoreAsyncResponse` payload; use `@Valid` to enforce DTO validation and log basic request details at debug level.

10. Define the marker annotation `@PerformanceMonitored` in the `monitoring` package, targeting methods and types with runtime retention and documenting that it enables execution-time measurement via the monitoring aspect.

11. Create the `ServiceMonitoringAspect` class annotated with `@Aspect` and `@Component`. Inside, define a pointcut `anyServiceOperation()` matching all public methods in `com.example.scoring.service..*` and an `@Around` advice `logAndTime` that logs method entry/exit with arguments and, when the method or type is annotated with `@PerformanceMonitored`, measures execution time using `System.nanoTime()` and logs the duration.

12. In `ServiceMonitoringAspect.logAndTime`, determine whether the join point is performance-monitored by checking for `@PerformanceMonitored` on both the method and its declaring class using `AnnotationUtils.findAnnotation`, build a safe, truncated argument string with `Arrays.deepToString`, log a `[SERVICE-ENTRY]` record, execute the method, then log either a timed `[SERVICE-EXIT]` (with durationMs and resultType) or an untimed exit when not annotated; also catch and log exceptions with `[SERVICE-ERROR]` and rethrow them.

13. Add `MonitoringConfiguration` annotated with `@Configuration` and `@EnableAspectJAutoProxy(proxyTargetClass = true)` to explicitly enable Spring AOP proxying and centralize future monitoring-related configuration such as metrics, tracing, or correlation IDs.

14. Create an `application.yml` configuration for an in-memory H2 database (with `ddl-auto: update`) and set logging levels such that monitoring and service logs are visible (e.g., INFO for monitoring, DEBUG for services) while avoiding excessive verbosity for other packages.

15. Write a Spring Boot test class `ScoringServiceApplicationTests` annotated with `@SpringBootTest` that autowires `ScoreService` and `ServiceMonitoringAspect`, asserts that the context loads correctly, verifies synchronous score calculation returns expected values, and confirms asynchronous calculation completes successfully using `CompletableFuture.get` with a timeout, ensuring the monitoring layer does not break normal application behavior.

