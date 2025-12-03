package com.example.scoring.service.impl;

import com.example.scoring.domain.Score;
import com.example.scoring.dto.ScoreRequest;
import com.example.scoring.exception.ScoreCalculationException;
import com.example.scoring.monitoring.PerformanceMonitored;
import com.example.scoring.repository.ScoreRepository;
import com.example.scoring.service.ScoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Default implementation of {@link ScoreService}.
 */
@Service
public class ScoreServiceImpl implements ScoreService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScoreServiceImpl.class);

    private final ScoreRepository scoreRepository;

    public ScoreServiceImpl(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    @Override
    @Transactional
    @PerformanceMonitored
    public Score calculateScoreSync(ScoreRequest request) {
        LOGGER.debug("Starting synchronous score calculation for candidateId={}", request.getCandidateId());
        return performCalculationAndPersist(request, false);
    }

    @Override
    @Async
    public CompletableFuture<Score> calculateScoreAsync(ScoreRequest request) {
        // This method is intentionally not annotated with @PerformanceMonitored directly
        // because it returns immediately after scheduling work on another thread. Instead,
        // the core calculation method below is monitored so that actual business latency is captured.
        LOGGER.debug("Scheduling asynchronous score calculation for candidateId={}", request.getCandidateId());
        Score score = performCalculationAndPersist(request, true);
        return CompletableFuture.completedFuture(score);
    }

    /**
     * Core calculation logic shared by both sync and async entry points.
     */
    @Transactional
    @PerformanceMonitored
    protected Score performCalculationAndPersist(ScoreRequest request, boolean asyncCalculation) {
        validateRequest(request);

        try {
            double scoreValue = calculatePercentage(request.getTotalQuestions(), request.getCorrectAnswers());

            Score score = new Score(
                    request.getCandidateId(),
                    scoreValue,
                    request.getTotalQuestions(),
                    request.getCorrectAnswers(),
                    LocalDateTime.now(),
                    asyncCalculation
            );

            Score saved = scoreRepository.save(score);
            LOGGER.info("Score calculated for candidateId={} value={} async={}",
                    saved.getCandidateId(), saved.getScoreValue(), saved.isAsyncCalculation());
            return saved;
        } catch (RuntimeException ex) {
            LOGGER.error("Unexpected error while calculating score for candidateId={}",
                    request.getCandidateId(), ex);
            throw new ScoreCalculationException("Failed to calculate score", ex);
        }
    }

    private void validateRequest(ScoreRequest request) {
        Objects.requireNonNull(request, "ScoreRequest must not be null");
        if (request.getTotalQuestions() == null || request.getCorrectAnswers() == null) {
            throw new ScoreCalculationException("Total questions and correct answers are required");
        }
        if (request.getCorrectAnswers() > request.getTotalQuestions()) {
            throw new ScoreCalculationException("Correct answers cannot exceed total questions");
        }
    }

    private double calculatePercentage(int totalQuestions, int correctAnswers) {
        if (totalQuestions <= 0) {
            throw new ScoreCalculationException("Total questions must be positive");
        }
        // Simple percentage calculation. In a real-world scenario, this might be
        // replaced with more advanced scoring rules.
        return (correctAnswers * 100.0) / totalQuestions;
    }
}
