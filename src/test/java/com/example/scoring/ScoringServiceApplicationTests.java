package com.example.scoring;

import com.example.scoring.domain.Score;
import com.example.scoring.dto.ScoreRequest;
import com.example.scoring.monitoring.ServiceMonitoringAspect;
import com.example.scoring.service.ScoreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Basic smoke tests ensuring that the Spring context loads and that the
 * monitoring configuration does not interfere with normal application startup
 * or service invocation.
 */
@SpringBootTest
class ScoringServiceApplicationTests {

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private ServiceMonitoringAspect monitoringAspect;

    @Test
    void contextLoads() {
        assertThat(scoreService).as("ScoreService should be available").isNotNull();
        assertThat(monitoringAspect).as("Monitoring aspect should be available").isNotNull();
    }

    @Test
    void synchronousScoreCalculationWorks() {
        ScoreRequest request = new ScoreRequest("candidate-1", 10, 7);
        Score score = scoreService.calculateScoreSync(request);

        assertThat(score).isNotNull();
        assertThat(score.getCandidateId()).isEqualTo("candidate-1");
        assertThat(score.getScoreValue()).isEqualTo(70.0);
    }

    @Test
    void asynchronousScoreCalculationWorks() throws Exception {
        ScoreRequest request = new ScoreRequest("candidate-2", 5, 5);
        CompletableFuture<Score> future = scoreService.calculateScoreAsync(request);

        Score score = future.get(5, TimeUnit.SECONDS);
        assertThat(score).isNotNull();
        assertThat(score.getCandidateId()).isEqualTo("candidate-2");
        assertThat(score.isAsyncCalculation()).isTrue();
    }
}
